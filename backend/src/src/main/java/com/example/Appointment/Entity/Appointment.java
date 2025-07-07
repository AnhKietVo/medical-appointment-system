package com.example.Appointment.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column
    private String fullname;
    @Column
    private String gender;
    @Column
    private String age;
    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate appointmentDate;
    @Column
    private String phone;
    @Column
    private String diseases;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    @Column
    private String address;
    @Column
    private String status;

    public Appointment() {
    }

    public Appointment(int id, User user, String fullname, String gender, String age, LocalDate appointmentDate, String phone, String diseases, Doctor doctor, String address, String status) {
        this.id = id;
        this.user = user;
        this.fullname = fullname;
        this.gender = gender;
        this.age = age;
        this.appointmentDate = appointmentDate;
        this.phone = phone;
        this.diseases = diseases;
        this.doctor = doctor;
        this.address = address;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", user=" + user +
                ", fullname='" + fullname + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", appointmentDate='" + appointmentDate + '\'' +
                ", phone='" + phone + '\'' +
                ", diseases='" + diseases + '\'' +
                ", doctor=" + doctor +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
