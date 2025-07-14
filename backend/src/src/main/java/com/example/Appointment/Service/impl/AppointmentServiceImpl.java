package com.example.Appointment.Service.impl;

import com.example.Appointment.Convert.AppointmentConvert;
import com.example.Appointment.DTO.AppointmentDTO;
import com.example.Appointment.Entity.Appointment;
import com.example.Appointment.Repository.AppointmentRepo;
import com.example.Appointment.Service.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    AppointmentRepo appointmentRepo;
    AppointmentConvert appointmentConvert;

    public AppointmentServiceImpl(AppointmentRepo appointmentRepo, AppointmentConvert appointmentConvert) {
        this.appointmentRepo = appointmentRepo;
        this.appointmentConvert = appointmentConvert;
    }

    @Override
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentConvert.entityToDTO(appointmentRepo.findAll());
    }

    @Override
    public int countAppointmentsByDoctorIdAndStatus(int doctorId, String status) {
        return appointmentRepo.countAppointmentsByDoctorIdAndStatus(doctorId, status);
    }

    @Override
    public List<AppointmentDTO> getAllAppointmentsByDoctorId(int doctor_id) {
        return appointmentConvert.entityToDTO(appointmentRepo.findByDoctorId(doctor_id));
    }

    @Override
    public boolean updateComment(int id, String comment) {
        if (appointmentRepo.existsById(id)){
            appointmentRepo.updateAppointmentComment(id, comment);
            return true;
        }
        return false;
    }
    @Override
    public AppointmentDTO getAppointmentById(int id) {
        Optional<Appointment> opt = appointmentRepo.findById(id);
        return opt.map(appointmentConvert::entityToDTO).orElse(null);
    }

    @Override
    public void saveAppointment(AppointmentDTO appointmentDTO) {
        appointmentRepo.save(appointmentConvert.DTOtoEntity(appointmentDTO));
    }

    @Override
    public List<AppointmentDTO> getAllAppointmentsByUserId(int userId) {
        return appointmentConvert.entityToDTO(appointmentRepo.findByUserId(userId));
    }

    @Override
    public boolean deleteAppointment(int id) {
        if (appointmentRepo.existsById(id)) {
            appointmentRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAppointment(AppointmentDTO appointmentDTO) {
        if (appointmentRepo.existsById(appointmentDTO.getId())) {
            appointmentRepo.save(appointmentConvert.DTOtoEntity(appointmentDTO));
            return true;
        }
        return false;
    }
}
