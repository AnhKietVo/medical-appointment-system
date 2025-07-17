package com.example.appointment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appointment.Api.UserApi;
import com.example.appointment.Utils.AppUtils;
import com.example.appointment.Utils.AppUtils;

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

        changePasswordBtn.setOnClickListener(v -> handleChangePassword());
    }

    private void handleChangePassword() {
        String oldPass = oldPassword.getText().toString().trim();
        String newPass = newPassword.getText().toString().trim();

        // Validate
        if (oldPass.isEmpty() || newPass.isEmpty() ) {
            AppUtils.showToast(this, "Vui lòng nhập đầy đủ thông tin");
            return;
        }

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            AppUtils.showToast(this, "Người dùng chưa đăng nhập");
            return;
        }

        try {
            JSONObject data = new JSONObject();
            data.put("userId", userId);
            data.put("oldPassword", oldPass);
            data.put("newPassword", newPass);

            UserApi.changePassword(this, data,
                    response -> AppUtils.showToast(this, "Đổi mật khẩu thành công!"),
                    error -> AppUtils.showToast(this, "Mật khẩu cũ không đúng!")
            );
        } catch (JSONException e) {
            e.printStackTrace();
            AppUtils.showToast(this, "Lỗi xử lý dữ liệu");
        }
    }
}
