package com.example.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appointment.Api.UserApi;
import com.example.appointment.Utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateAccountActivity extends AppCompatActivity {

    EditText editTextName, editTextEmail, editTextPassword;
    Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(v -> handleRegister());
    }

    private void handleRegister() {
        String fullName = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Kiểm tra hợp lệ
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            AppUtils.showToast(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        if (!AppUtils.isValidFullName(fullName)) {
            AppUtils.showToast(this, "Tên không hợp lệ (chỉ chứa chữ cái và khoảng trắng)");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            AppUtils.showToast(this,"Email không hợp lệ");
            return;
        }


        // Format tên
        String formattedName = AppUtils.formatFullName(fullName);

        JSONObject registerData = new JSONObject();
        try {
            registerData.put("fullName", formattedName);
            registerData.put("email", email);
            registerData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            AppUtils.showToast(this,"Lỗi tạo dữ liệu JSON");
            return;
        }

        UserApi.register(this, registerData, response -> {
            AppUtils.showToast(this,"Đăng kí tài khoản thành công!");
            startActivity(new Intent(this, UserLoginActivity.class));
            finish();
        }, error -> {
            String errorMsg = "Đăng kí tài khoản thất bại";
            if (error.networkResponse != null && error.networkResponse.statusCode == 409) {
                errorMsg = "Email đã tồn tại";
            }
            AppUtils.showToast(this,errorMsg);
        });
    }
}
