package com.example.Appointment.Repository;

import com.example.Appointment.Entity.Appointment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Integer> {
    long count();
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId")
    int countAppointmentsByDoctorId(@Param("doctorId") int doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId ORDER BY a.appointmentDate DESC")
    List<Appointment> findByDoctorId(@Param("doctorId") int doctorId);

    @Query("SELECT a FROM Appointment a WHERE a.user.id = :userId ORDER BY a.appointmentDate DESC")
    List<Appointment> findByUserId(@Param("userId") int userId);

    @Modifying
    @Transactional
    @Query("UPDATE Appointment a SET a.status = :comment WHERE a.id = :id")
    int updateAppointmentComment(@Param("id") int id, @Param("comment") String comment);


}
