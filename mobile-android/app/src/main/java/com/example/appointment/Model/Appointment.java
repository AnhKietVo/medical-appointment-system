package com.example.appointment.Model;

public class Appointment {
    public int id;
    public String fullname;
    public String gender;
    public int age;
    public String appointmentDate;
    public String phone;
    public String diseases;
    public String doctorName;
    public String address;
    public String status;

    public Appointment() {
    }

    public Appointment(int id, String fullname, String gender, int age, String appointmentDate, String phone, String diseases, String doctorName, String address, String status) {
        this.id = id;
        this.fullname = fullname;
        this.gender = gender;
        this.age = age;
        this.appointmentDate = appointmentDate;
        this.phone = phone;
        this.diseases = diseases;
        this.doctorName = doctorName;
        this.address = address;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
