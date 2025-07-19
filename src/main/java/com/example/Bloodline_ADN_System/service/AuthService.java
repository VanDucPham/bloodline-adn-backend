package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.Login.LoginRequest;
import com.example.Bloodline_ADN_System.dto.Login.LoginResponse;
import com.example.Bloodline_ADN_System.dto.noneWhere.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse loginWithGoogle(String idToken);;
    String RegisterUser(RegisterRequest registerRequest);
}
