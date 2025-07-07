package com.example.Appointment.Service;

import com.example.Appointment.DTO.DoctorDTO;

import java.util.List;

public interface DoctorService {
    void saveDoctor(DoctorDTO doctorDTO);
    List<DoctorDTO> getAllDoctors();
    public boolean deleteDoctor(int id);
    public DoctorDTO getDoctorById(int id);
    public boolean updateDoctor(DoctorDTO DoctorDTO);
    public DoctorDTO login(String email, String password);
    int countAllDoctors();
}
