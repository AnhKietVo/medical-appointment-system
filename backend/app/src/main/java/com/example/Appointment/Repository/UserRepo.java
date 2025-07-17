package com.example.Appointment.Repository;

import com.example.Appointment.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmailAndPassword(String email, String password);
    long count();
    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :newPassword WHERE u.id = :userId")
    int updatePassword(int userId, String newPassword);

    @Query("SELECT u.password FROM User u WHERE u.id = :userId")
    String getPasswordById(int userId);
}
