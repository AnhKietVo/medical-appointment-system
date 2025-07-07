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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

public class UserAppointmentActivity extends AppCompatActivity {
    private EditText fullName, age, date, phone, diseases, address;
    private Spinner genderSpinner, doctorSpinner;
    private Button submitButton;

    private Map<String, Integer> doctorMap = new HashMap<>();
    private long userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_appointment);

        fullName = findViewById(R.id.editTextFullName);
        age = findViewById(R.id.editTextAge);
        date = findViewById(R.id.editTextAppointmentDate);
        phone = findViewById(R.id.editTextPhone);
        diseases = findViewById(R.id.editTextDiseases);
        address = findViewById(R.id.editTextAddress);
        genderSpinner = findViewById(R.id.spinnerGender);
        doctorSpinner = findViewById(R.id.spinnerDoctor);
        submitButton = findViewById(R.id.buttonSubmit);

        setupGenderSpinner();
        loadDoctors();

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
        }
        date.setOnClickListener(v -> showDatePicker());

        submitButton.setOnClickListener(v -> submitAppointment());
    }
    private void setupGenderSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Nam", "Nữ"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this,
                (view, year, month, day) -> {
                    month++;
                    String formatted = String.format(Locale.US, "%02d/%02d/%04d", day, month, year);
                    date.setText(formatted);
                },
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
        ).show();
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
                        doctorSpinner.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
        );
    }
    private void submitAppointment() {
        String name = fullName.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String ageStr = age.getText().toString().trim();
        String dateStr = date.getText().toString().trim();
        String phoneStr = phone.getText().toString().trim();
        String diseasesStr = diseases.getText().toString().trim();
        String addressStr = address.getText().toString().trim();
        String selectedDoctorName = doctorSpinner.getSelectedItem().toString();
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
            data.put("address", addressStr);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi nhập dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        UserApi.submitAppointment(this, data, response -> {
            Toast.makeText(this, "Đặt lịch hẹn thành công!", Toast.LENGTH_SHORT).show();
            clearFields();
        }, error -> {
            Toast.makeText(this, "Đặt lịch hẹn thất bại!", Toast.LENGTH_SHORT).show();
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


    private void clearFields() {
        fullName.setText("");
        genderSpinner.setSelection(0);
        age.setText("");
        date.setText("");
        phone.setText("");
        diseases.setText("");
        doctorSpinner.setSelection(0);
        address.setText("");
    }

}