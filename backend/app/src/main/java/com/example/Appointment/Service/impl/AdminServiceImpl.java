package com.example.Appointment.Service.impl;

import com.example.Appointment.Convert.UserConvert;
import com.example.Appointment.Repository.AppointmentRepo;
import com.example.Appointment.Repository.DoctorRepo;
import com.example.Appointment.Repository.UserRepo;
import com.example.Appointment.Service.AdminService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {
    UserRepo userRepo;
    DoctorRepo doctorRepo;
    AppointmentRepo appointmentRepo;
    UserConvert userConvert;

    public AdminServiceImpl(UserRepo userRepo, DoctorRepo doctorRepo, AppointmentRepo appointmentRepo, UserConvert userConvert) {
        this.userRepo = userRepo;
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
        this.userConvert = userConvert;
    }

    @Override
    public Map<String, Integer> getDashboardStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("doctors", (int) doctorRepo.count());
        stats.put("users", (int) userRepo.count());
        stats.put("appointments", (int) appointmentRepo.count());
        return stats;
    }

}
