package com.example.Bloodline_ADN_System.dto.Login;

import lombok.Data;

@Data
public class LoginResponse {
    private Long user_Id;
    private String token;
    private String message ;
    private String fullName ;
    private String role ;
    private String email ;
    public LoginResponse(String token, String message, Long user_Id, String fullName, String role, String email) {
        this.token = token;
        this.message = message;
        this.user_Id = user_Id;
        this.fullName = fullName;
        this.role = role;
        this.email = email;
    }
}
