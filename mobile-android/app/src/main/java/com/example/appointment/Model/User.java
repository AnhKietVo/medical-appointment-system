package com.example.appointment.Model;

public class User {
    private int user_id;

    private String fullName;

    private String email;

    private String password;

    public User() {
    }

    public User(int user_id, String fullName, String email, String password) {
        this.user_id = user_id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
