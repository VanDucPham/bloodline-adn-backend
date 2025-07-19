package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.noneWhere.CreateUserRequest;
import com.example.Bloodline_ADN_System.dto.noneWhere.accountResponse;
import com.example.Bloodline_ADN_System.dto.noneWhere.updateUserRequest;

import java.util.List;

public interface AdminService {
    void createUser(CreateUserRequest request);
    List<accountResponse> getAllUsers();
    void updateUser(Long id, updateUserRequest response);

    void deleteUser(Long id);
}
