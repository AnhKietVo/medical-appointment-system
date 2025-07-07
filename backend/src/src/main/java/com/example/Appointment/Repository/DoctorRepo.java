package com.example.Appointment.Repository;

import com.example.Appointment.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<Doctor, Integer> {
    long count();
    Doctor findByEmailAndPassword(String email, String password);
}
