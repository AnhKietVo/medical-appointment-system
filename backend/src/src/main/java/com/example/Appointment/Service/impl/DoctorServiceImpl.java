package com.example.Appointment.Service.impl;

import com.example.Appointment.Convert.DoctorConvert;
import com.example.Appointment.DTO.DoctorDTO;
import com.example.Appointment.Entity.Appointment;
import com.example.Appointment.Entity.Doctor;
import com.example.Appointment.Repository.DoctorRepo;
import com.example.Appointment.Service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService {
    DoctorRepo doctorRepo;
    DoctorConvert doctorConvert;

    public DoctorServiceImpl(DoctorRepo doctorRepo, DoctorConvert doctorConvert) {
        this.doctorRepo = doctorRepo;
        this.doctorConvert = doctorConvert;
    }

    @Override
    public void saveDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = doctorConvert.dtoToEntity(doctorDTO);
        doctorRepo.save(doctor);
    }

    @Override
    public List<DoctorDTO> getAllDoctors() {
        return doctorConvert.entityToDTO(doctorRepo.findAll());
    }

    @Override
    public boolean deleteDoctor(int id) {
        if (doctorRepo.existsById(id)) {
            doctorRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public DoctorDTO getDoctorById(int id) {
        Optional<Doctor> opt = doctorRepo.findById(id);
        return opt.map(doctorConvert::entityToDTO).orElse(null);
    }

    @Override
    public boolean updateDoctor(DoctorDTO doctorDTO) {
        if (doctorRepo.existsById(doctorDTO.getDoctor_id())) {
            doctorRepo.save(doctorConvert.dtoToEntity(doctorDTO));
            return true;
        }
        return false;
    }

    @Override
    public DoctorDTO login(String email, String password) {
        Doctor doctor = doctorRepo.findByEmailAndPassword(email, password);
        if (doctor != null) {
            return doctorConvert.entityToDTO(doctor);
        } else {
            return null;
        }

    }

    @Override
    public int countAllDoctors() {
        return (int)doctorRepo.count();
    }
}
