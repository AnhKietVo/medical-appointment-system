package com.example.Appointment.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class AppointmentDTO {
    private int id;
    private String fullname;
    private String gender;
    private String age;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate appointmentDate;
    private String phone;
    private String diseases;
    private Integer doctorId;
    private Integer userId;
    private String doctorName;
    private String address;
    private String status;

    public AppointmentDTO() {
    }

    public AppointmentDTO(int id, String fullname, String gender, String age, LocalDate appointmentDate, String phone, String diseases, Integer doctorId, Integer userId, String doctorName, String address, String status) {
        this.id = id;
        this.fullname = fullname;
        this.gender = gender;
        this.age = age;
        this.appointmentDate = appointmentDate;
        this.phone = phone;
        this.diseases = diseases;
        this.doctorId = doctorId;
        this.userId = userId;
        this.doctorName = doctorName;
        this.address = address;
        this.status = status;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }



    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}


