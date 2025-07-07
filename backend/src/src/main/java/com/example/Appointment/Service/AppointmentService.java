package com.example.Appointment.Service;

import com.example.Appointment.DTO.AppointmentDTO;

import java.util.List;

public interface AppointmentService {
    List<AppointmentDTO> getAllAppointments();
    int countAppointmentsByDoctorId(int doctor_id);
    List<AppointmentDTO> getAllAppointmentsByDoctorId(int doctor_id);
    public boolean updateComment(int id, String comment);
    AppointmentDTO getAppointmentById(int id);
    void saveAppointment(AppointmentDTO appointmentDTO);
    public List<AppointmentDTO> getAllAppointmentsByUserId(int userId);
    public boolean deleteAppointment(int id);
    public boolean updateAppointment(AppointmentDTO appointmentDTO);
    }
