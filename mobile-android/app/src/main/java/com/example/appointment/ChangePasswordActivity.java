package com.example.appointment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appointment.Api.UserApi;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPassword, newPassword, confirmPassword;
    private Button changePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassword = findViewById(R.id.editTextOldPassword);
        newPassword = findViewById(R.id.editTextNewPassword);
        changePasswordBtn = findViewById(R.id.buttonChangePassword);

        changePasswordBtn.setOnClickListener(v -> {
            String oldPass = oldPassword.getText().toString().trim();
            String newPass = newPassword.getText().toString().trim();
            if (oldPass.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            int userId = prefs.getInt("userId", -1);

            if (userId == -1) {
                Toast.makeText(this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject data = new JSONObject();
            try {
                data.put("userId", userId);
                data.put("oldPassword", oldPass);
                data.put("newPassword", newPass);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error creating request data", Toast.LENGTH_SHORT).show();
                return;
            }

            UserApi.changePassword(this, data,
                    response -> {
                        Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Toast.makeText(this, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
                    }
            );
        });
    }
}
