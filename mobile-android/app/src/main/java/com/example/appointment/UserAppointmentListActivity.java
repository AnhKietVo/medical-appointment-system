package com.example.appointment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appointment.Adapter.AppointmentAdapter;
import com.example.appointment.Api.UserApi;
import com.example.appointment.Model.Appointment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserAppointmentListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private List<Appointment> appointmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment_list);

        recyclerView = findViewById(R.id.appointmentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AppointmentAdapter(this, appointmentList,
                this::deleteAppointment, this::editAppointment);
        recyclerView.setAdapter(adapter);

        loadAppointments();
    }

    private void loadAppointments() {
        UserApi.getAppointments(this, response -> {
            try {
                JSONArray array = response.getJSONArray("data");
                appointmentList.clear();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Appointment app = new Appointment();
                    app.id = obj.getInt("id");
                    app.fullname = obj.getString("fullname");
                    app.gender = obj.getString("gender");
                    app.age = obj.getInt("age");
                    app.appointmentDate = obj.getString("appointmentDate");
                    app.phone = obj.getString("phone");
                    app.diseases = obj.getString("diseases");
                    app.doctorName = obj.getString("doctorName");
                    app.status = obj.getString("status");
                    appointmentList.add(app);
                }
                Collections.sort(appointmentList, (a1, a2) -> {
                    boolean isWaiting1 = "Chờ duyệt".equalsIgnoreCase(a1.status);
                    boolean isWaiting2 = "Chờ duyệt".equalsIgnoreCase(a2.status);

                    // Nếu a1 là Waiting mà a2 không phải → a1 đứng trước (-1)
                    // Nếu a2 là Waiting mà a1 không phải → a2 đứng trước (1)
                    // Nếu cả hai đều giống nhau → không đổi thứ tự (0)
                    return Boolean.compare(!isWaiting1, !isWaiting2);
                });

                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "JSON parsing error", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            error.printStackTrace();
            if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                Toast.makeText(this, "Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, UserLoginActivity.class));
                finish(); // đóng activity hiện tại
            } else {
                Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deleteAppointment(int id) {
        new AlertDialog.Builder(this)
                .setMessage("Bạn có chắc chắn muốn xóa lịch hẹn này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    UserApi.deleteAppointment(this, id,
                            response -> {
                                Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                loadAppointments();
                            },
                            error -> Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Không", null)
                .show();
    }


    private void editAppointment(Appointment app) {
        Intent intent = new Intent(this, EditAppointmentActivity.class);
        intent.putExtra("appointmentId", app.id);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadAppointments(); // Hàm reload danh sách
        }
    }

}
