package com.example.appointment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

        AppUtils.setupGenderSpinner(this, genderSpinner);
        AppUtils.loadDoctors(this, doctorSpinner, doctorMap);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
        }
        date.setOnClickListener(v -> AppUtils.showDatePicker(this, date));

        submitButton.setOnClickListener(v -> submitAppointment());
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