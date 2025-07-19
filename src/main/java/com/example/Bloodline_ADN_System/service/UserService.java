package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.dto.noneWhere.ChangePasswordDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.UserUpdateDTO;

import java.util.Optional;

public interface UserService {
    UserUpdateDTO updateUser(String email, UserUpdateDTO updatedUser);
    void changePassword(Long userId, ChangePasswordDTO changePasswordDTO);
    Optional<User> getUserById(Long id);
    UserUpdateDTO getUserByEmail(String email);


    Optional<User> findUserById(Long id);
}
