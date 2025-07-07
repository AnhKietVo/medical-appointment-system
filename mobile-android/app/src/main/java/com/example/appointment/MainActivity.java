package com.example.appointment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.appointment.Adapter.CarouselAdapter;
import com.example.appointment.Adapter.TeamAdapter;
import com.example.appointment.Api.UserApi;
import com.example.appointment.Model.DoctorTeam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 carouselViewPager;
    private boolean isLoggedIn = false;
    private String fullName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        isLoggedIn = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getBoolean("loggedIn", false);
        fullName = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getString("fullName", "");
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Trang chủ");
        }
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        //
        carouselViewPager = findViewById(R.id.carouselViewPager);

        List<Integer> imageResIds = Arrays.asList(
                R.drawable.hopital,
                R.drawable.hopital1,
                R.drawable.hopital2,
                R.drawable.hopital3
        );

        CarouselAdapter carouselAdapter = new CarouselAdapter(this, imageResIds);
        carouselViewPager.setAdapter(carouselAdapter);

        // Optional: auto-scroll
        carouselViewPager.postDelayed(new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (carouselAdapter.getItemCount() == 0) return;
                currentPage = (currentPage + 1) % carouselAdapter.getItemCount();
                carouselViewPager.setCurrentItem(currentPage, true);
                carouselViewPager.postDelayed(this, 3000);
            }
        }, 3000);

        //
        RecyclerView recyclerViewTeam = findViewById(R.id.recyclerViewTeam);
        recyclerViewTeam.setLayoutManager(new GridLayoutManager(this, 2)); // 2 cột

        List<DoctorTeam> doctorList = new ArrayList<>();
        doctorList.add(new DoctorTeam("BS. Đoàn Trọng Nghĩa", "(Trưởng khoa)", R.drawable.doctor1));
        doctorList.add(new DoctorTeam("BS. Nguyễn Hành Trang", "(Trưởng khoa)", R.drawable.doctor2));
        doctorList.add(new DoctorTeam("BS. Trịnh Thu Dung", "(Trưởng khoa)", R.drawable.doctor3));
        doctorList.add(new DoctorTeam("BS. Hà Nhật Anh", "(Trưởng khoa)", R.drawable.doctor4));

        TeamAdapter teamAdapter = new TeamAdapter(doctorList);
        recyclerViewTeam.setAdapter(teamAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_login);
        SpannableString spanString = new SpannableString(item.getTitle());
        spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0);
        item.setTitle(spanString);
        if (isLoggedIn) {
            MenuItem loginItem = menu.findItem(R.id.action_login);
            loginItem.setVisible(false); // ẩn nút Login

            MenuItem profileItem = menu.findItem(R.id.action_change_password);
            MenuItem appointment = menu.findItem(R.id.action_make_appointment);
            MenuItem myAppointment = menu.findItem(R.id.action_my_appointment);
            MenuItem logout = menu.findItem(R.id.action_logout);
            profileItem.setVisible(true);
            appointment.setVisible(true);
            myAppointment.setVisible(true);
            logout.setVisible(true);
        }
        if (!fullName.isEmpty()) {
            MenuItem welcomeItem = menu.findItem(R.id.action_welcome);
            welcomeItem.setVisible(true);
            welcomeItem.setTitle(fullName);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_login) {
            Intent intent = new Intent(this, UserLoginActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        if (item.getItemId() == R.id.action_change_password) {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_make_appointment) {
            Intent intent = new Intent(this, UserAppointmentActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.action_my_appointment) {
            Intent intent = new Intent(this, UserAppointmentListActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void logout() {
        UserApi.logout(this,
                response -> {
                    // Clear SharedPreferences
                    getSharedPreferences("UserPrefs", MODE_PRIVATE)
                            .edit()
                            .clear()
                            .apply();

                    Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

                    // Quay lại màn hình login
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                },
                error -> {
                    Toast.makeText(this, "Đăng xuất thất bại: " + error.toString(), Toast.LENGTH_SHORT).show();
                });
    }
}