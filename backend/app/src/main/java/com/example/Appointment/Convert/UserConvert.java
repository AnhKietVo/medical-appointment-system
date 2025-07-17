package com.example.Appointment.Convert;

import com.example.Appointment.DTO.UserDTO;
import com.example.Appointment.Entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConvert {
    public UserDTO entityToDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setUser_id(user.getUser_id());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        return userDTO;
    }

    public List<UserDTO> entityToDTO(List<User> users){
        return users.stream().map(x -> entityToDTO(x)).collect(Collectors.toList());
    }

    public User dtoToEntity(UserDTO userDTO){
        User user = new User();
        user.setUser_id(userDTO.getUser_id());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    public List<User> dtoToEntity(List<UserDTO> userDTOS){
        return userDTOS.stream().map(x -> dtoToEntity(x)).collect(Collectors.toList());
    }
}
