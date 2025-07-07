package com.example.appointment.Api;

import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.init(this); // 👈 khởi tạo duy nhất ở đây
    }
}
