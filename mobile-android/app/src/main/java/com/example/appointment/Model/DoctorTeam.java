package com.example.appointment.Model;

public class DoctorTeam {
    private String name;
    private String title;
    private int imageResId;

    public DoctorTeam(String name, String title, int imageResId) {
        this.name = name;
        this.title = title;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getTitle() { return title; }
    public int getImageResId() { return imageResId; }
}
