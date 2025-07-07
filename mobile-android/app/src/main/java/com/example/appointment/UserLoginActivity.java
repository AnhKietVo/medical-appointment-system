package com.example.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.appointment.Api.ApiClient;
import com.example.appointment.Api.UserApi;

import org.json.JSONException;
import org.json.JSONObject;

public class UserLoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    TextView createAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_login);

        ApiClient.init(this); // Đảm bảo RequestQueue được khởi tạo

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(v -> {
            startActivity(new Intent(UserLoginActivity.this, CreateAccountActivity.class));
        });
        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            UserApi.login(this, email, password,
                    response -> {
                        try {
                            if ("Đăng nhập thành công!".equals(response.getString("message"))) {
                                JSONObject user = response.getJSONObject("data");
                                Toast.makeText(this, "Chào " + user.getString("fullName"), Toast.LENGTH_SHORT).show();

                                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                        .edit()
                                        .putBoolean("loggedIn", true)
                                        .putString("fullName", user.getString("fullName"))
                                        .putInt("userId", user.getInt("user_id"))
                                        .apply();

                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Toast.makeText(this, "Đăng nhập thất bại: " + error.toString(), Toast.LENGTH_SHORT).show()
            );
        });
    }
}

