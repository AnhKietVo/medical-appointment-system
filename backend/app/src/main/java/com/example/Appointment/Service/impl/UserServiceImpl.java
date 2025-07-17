package com.example.Appointment.Service.impl;

import com.example.Appointment.Convert.UserConvert;
import com.example.Appointment.DTO.UserDTO;
import com.example.Appointment.Entity.User;
import com.example.Appointment.Repository.UserRepo;
import com.example.Appointment.Service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    UserRepo userRepo;
    UserConvert userConvert;

    public UserServiceImpl(UserRepo userRepo, UserConvert userConvert) {
        this.userRepo = userRepo;
        this.userConvert = userConvert;
    }

    @Override
    public UserDTO getUserById(int userid) {
        Optional<User> optionalUser = userRepo.findById(userid);
        if (optionalUser.isPresent()) {
            return userConvert.entityToDTO(optionalUser.get());
        } else {
            return null;
        }
    }

    @Override
    public UserDTO login(String email, String password) {
        User user = userRepo.findByEmailAndPassword(email, password);
        if (user != null) {
            return userConvert.entityToDTO(user);
        } else {
            return null;
        }
    }

    @Override
    public boolean registerUser(UserDTO userDTO) {
        if (userRepo.existsByEmail(userDTO.getEmail())){
            return false; // Email đã tồn tại
        }
        userRepo.save(userConvert.dtoToEntity(userDTO));
        return true;
    }

    @Override
    public boolean changeUserPassword(int userId, String oldPassword, String newPassword) {
        String currentPassword = userRepo.getPasswordById(userId);
        if (currentPassword != null && currentPassword.equals(oldPassword)) {
            userRepo.updatePassword(userId, newPassword);
            return true;
        }
        return false;
    }
//    @Override
//    public List<User> getAllUser() {
//        return userRepo.findAll();
//    }
}
