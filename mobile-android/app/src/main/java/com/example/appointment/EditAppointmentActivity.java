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
import com.example.appointment.Utils.AppUtils;

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
        AppUtils.setupGenderSpinner(this, spinnerGender);
        AppUtils.loadDoctors(this, spinnerDoctor, doctorMap);
        loadAppointment();
        editTextAppointmentDate.setOnClickListener(v -> AppUtils.showDatePicker(this, editTextAppointmentDate));
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

                        AppUtils.setSpinnerSelection(spinnerGender, gender);
                        AppUtils.setSpinnerSelection(spinnerDoctor, doctorName);

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

        if (name.isEmpty() || gender.isEmpty() || ageStr.isEmpty() || dateStr.isEmpty() || phoneStr.isEmpty() ||
                diseasesStr.isEmpty() || addressStr.isEmpty() || selectedDoctorName.isEmpty()
        ) {
            AppUtils.showToast(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        if (!AppUtils.isValidFullName(name)) {
            AppUtils.showToast(this, "Tên không hợp lệ (chỉ chứa chữ cái và khoảng trắng)");
            return;
        }
        if (!AppUtils.isValidAge(ageStr)) {
            AppUtils.showToast(this, "Tuổi phải 1-120 ");
            return;
        }
        if (!AppUtils.isValidFutureDate(dateStr)) {
            AppUtils.showToast(this, "Không chọn ngày nhỏ hơn hiện tại");
            return;
        }
        if (!AppUtils.isValidPhone(phoneStr)) {
            AppUtils.showToast(this, "Từ 9 - 11 số");
            return;
        }
        String formattedName = AppUtils.formatFullName(name);
        JSONObject data = new JSONObject();
        try {
            data.put("userId", userId);
            data.put("fullname", formattedName);
            data.put("gender", gender);
            data.put("age", Integer.parseInt(ageStr));
            data.put("appointmentDate", AppUtils.formatDateForAPI(dateStr));
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

}
