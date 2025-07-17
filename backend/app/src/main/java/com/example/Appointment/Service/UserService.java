package com.example.Appointment.Service;

import com.example.Appointment.DTO.UserDTO;

public interface UserService {
    public UserDTO getUserById(int userid);
//    public List<User> getAllUser();
    public UserDTO login(String email, String password);
    boolean registerUser(UserDTO userDTO);
    public boolean changeUserPassword(int userId, String oldPassword, String newPassword);
}
