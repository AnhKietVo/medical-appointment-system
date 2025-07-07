package com.example.appointment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appointment.Api.UserApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditAppointmentActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextAge, editTextAppointmentDate, editTextPhone, editTextDiseases, editTextAddress;
    private Spinner spinnerGender, spinnerDoctor;
    private Button buttonUpdate;

    private int appointmentId;
    private Map<String, Integer> doctorMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_appointment);

        appointmentId = getIntent().getIntExtra("appointmentId", -1);
        if (appointmentId == -1) {
            Toast.makeText(this, "ID không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupSpinners();
        loadDoctors();
        loadAppointment();
        editTextAppointmentDate.setOnClickListener(v -> showDatePicker());
        buttonUpdate.setOnClickListener(v -> updateAppointment());
    }

    private void initViews() {
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextAge = findViewById(R.id.editTextAge);
        editTextAppointmentDate = findViewById(R.id.editTextAppointmentDate);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDiseases = findViewById(R.id.editTextDiseases);
        editTextAddress = findViewById(R.id.editTextAddress);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        buttonUpdate = findViewById(R.id.buttonUpdate);
    }

    private void setupSpinners() {
        String[] genders = {"Nam", "Nữ"};
        spinnerGender.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders));

        // TODO: Fetch doctor list from server for spinnerDoctor
        String[] doctors = {"Dr. John", "Dr. Alice", "Dr. Smith"};
        spinnerDoctor.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doctors));
    }
    private void loadDoctors() {
        UserApi.getDoctors(this,
                response -> {
                    try {
                        JSONArray array = response.getJSONArray("data");
                        List<String> doctorNames = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            int id = obj.getInt("doctor_id");
                            String name = obj.getString("fullName") + " (" + obj.getString("specialist") + ")";
                            doctorMap.put(name, id);
                            doctorNames.add(name);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                android.R.layout.simple_spinner_item, doctorNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDoctor.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
        );
    }
    private void loadAppointment() {
        UserApi.getAppointmentById(this, appointmentId,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        editTextFullName.setText(data.getString("fullname"));
                        editTextAge.setText(String.valueOf(data.getInt("age")));
                        editTextAppointmentDate.setText(data.getString("appointmentDate"));
                        editTextPhone.setText(data.getString("phone"));
                        editTextDiseases.setText(data.getString("diseases"));
                        editTextAddress.setText(data.getString("address"));

                        String gender = data.getString("gender");
                        String doctorName = data.getString("doctorName");

                        setSpinnerSelection(spinnerGender, gender);
                        setSpinnerSelection(spinnerDoctor, doctorName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                });
    }
    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) -> {
                    month++;
                    String formatted = String.format(Locale.US, "%02d/%02d/%04d", day, month, year);
                    editTextAppointmentDate.setText(formatted);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
    private void updateAppointment() {
        String name = editTextFullName.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String ageStr = editTextAge.getText().toString().trim();
        String dateStr = editTextAppointmentDate.getText().toString().trim();
        String phoneStr = editTextPhone.getText().toString().trim();
        String diseasesStr = editTextDiseases.getText().toString().trim();
        String addressStr = editTextAddress.getText().toString().trim();
        String selectedDoctorName = spinnerDoctor.getSelectedItem().toString();

        if (!doctorMap.containsKey(selectedDoctorName)) {
            Toast.makeText(this, "Chọn bác sĩ", Toast.LENGTH_SHORT).show();
            return;
        }

        int doctorId = doctorMap.get(selectedDoctorName);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(dateStr) || !dateStr.contains("/")) {
            Toast.makeText(this, "Chọn lịch hẹn", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject data = new JSONObject();
        try {
            data.put("userId", userId);
            data.put("fullname", name);
            data.put("gender", gender);
            data.put("age", Integer.parseInt(ageStr));
            data.put("appointmentDate", formatDateForAPI(dateStr));
            data.put("phone", phoneStr);
            data.put("diseases", diseasesStr);
            data.put("doctorId", doctorId);
            data.put("doctorName", selectedDoctorName);
            data.put("address", addressStr);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi nhập dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        UserApi.updateAppointment(this, appointmentId, data, response -> {
            Toast.makeText(this, "Chỉnh sửa lịch hẹn thành công!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish(); // quay lại activity trước

        }, error -> {
            Toast.makeText(this, "Chỉnh sửa lịch hẹn thất bại!", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        });
    }


    private String formatDateForAPI(String dateStr) {
        if (dateStr == null || !dateStr.contains("/")) {
            return dateStr;
        }

        String[] parts = dateStr.split("/"); // dd/MM/yyyy
        if (parts.length != 3) {
            return dateStr;
        }

        return parts[0] + "/" + parts[1] + "/" + parts[2]; // vẫn là dd/MM/yyyy (nhưng giờ an toàn hơn)
    }
    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (value.equalsIgnoreCase(adapter.getItem(i).toString())) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
