package com.example.Appointment.Convert;

import com.example.Appointment.DTO.AppointmentDTO;
import com.example.Appointment.Entity.Appointment;
import com.example.Appointment.Entity.Doctor;
import com.example.Appointment.Entity.User;
import com.example.Appointment.Repository.DoctorRepo;
import com.example.Appointment.Repository.UserRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentConvert {
    DoctorRepo doctorRepo;
    UserRepo userRepo;

    public AppointmentConvert(DoctorRepo doctorRepo, UserRepo userRepo) {
        this.doctorRepo = doctorRepo;
        this.userRepo = userRepo;
    }

    public AppointmentDTO entityToDTO(Appointment appointment){
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setFullname(appointment.getFullname());
        appointmentDTO.setGender(appointment.getGender());
        appointmentDTO.setAge(appointment.getAge());
        appointmentDTO.setAppointmentDate(appointment.getAppointmentDate());
        appointmentDTO.setPhone(appointment.getPhone());
        appointmentDTO.setDiseases(appointment.getDiseases());
        appointmentDTO.setDoctorId(appointment.getDoctor().getDoctor_id());
        appointmentDTO.setDoctorName(appointment.getDoctor().getFullName());
        appointmentDTO.setAddress(appointment.getAddress());
        appointmentDTO.setStatus(appointment.getStatus());
        appointmentDTO.setUserId(appointment.getUser().getUser_id());
        return appointmentDTO;
    }

    public List<AppointmentDTO> entityToDTO(List<Appointment> appointments){
        return appointments.stream().map(x -> entityToDTO(x)).collect(Collectors.toList());
    }

    public Appointment DTOtoEntity(AppointmentDTO appointmentDTO){
        Appointment appointment = new Appointment();
        appointment.setId(appointmentDTO.getId());
        appointment.setFullname(appointmentDTO.getFullname());
        appointment.setGender(appointmentDTO.getGender());
        appointment.setAge(appointmentDTO.getAge());
        appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        appointment.setPhone(appointmentDTO.getPhone());
        appointment.setDiseases(appointmentDTO.getDiseases());
        appointment.setAddress(appointmentDTO.getAddress());
        appointment.setStatus(appointmentDTO.getStatus());
        User user = userRepo.findById(appointmentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        appointment.setUser(user);

        Doctor doctor = doctorRepo.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        appointment.setDoctor(doctor);
        return appointment;
    }

    public List<Appointment> DTOtoEntity(List<AppointmentDTO> appointmentDTOS){
        return appointmentDTOS.stream().map(x -> DTOtoEntity(x)).collect(Collectors.toList());
    }

}
