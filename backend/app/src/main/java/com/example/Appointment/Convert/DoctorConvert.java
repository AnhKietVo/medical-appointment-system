package com.example.Appointment.Convert;

import com.example.Appointment.DTO.DoctorDTO;
import com.example.Appointment.Entity.Doctor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DoctorConvert {
    public DoctorDTO entityToDTO(Doctor doctor){
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setDoctor_id(doctor.getDoctor_id());
        doctorDTO.setFullName(doctor.getFullName());
        doctorDTO.setEmail(doctor.getEmail());
        doctorDTO.setPassword(doctor.getPassword());
        doctorDTO.setDateOfBirth(doctor.getDateOfBirth());
        doctorDTO.setQualification(doctor.getQualification());
        doctorDTO.setPhone(doctor.getPhone());
        doctorDTO.setSpecialist(doctor.getSpecialist());
        return doctorDTO;
    }

    public List<DoctorDTO> entityToDTO(List<Doctor> doctors){
        return doctors.stream().map(x -> entityToDTO(x)).collect(Collectors.toList());
    }

    public Doctor dtoToEntity(DoctorDTO doctorDTO){
        Doctor doctor = new Doctor();
        doctor.setDoctor_id(doctorDTO.getDoctor_id());
        doctor.setFullName(doctorDTO.getFullName());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setPassword(doctorDTO.getPassword());
        doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
        doctor.setQualification(doctorDTO.getQualification());
        doctor.setPhone(doctorDTO.getPhone());
        doctor.setSpecialist(doctorDTO.getSpecialist());
        return doctor;
    }

    public List<Doctor> dtoToEntity(List<DoctorDTO> doctorDTOS){
        return doctorDTOS.stream().map(x -> dtoToEntity(x)).collect(Collectors.toList());
    }


}
