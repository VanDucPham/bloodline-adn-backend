package com.example.Bloodline_ADN_System.dto.noneWhere;

import lombok.Data;

@Data
public class RegisterRequest {
    private String fullName ;
    private String email ;
    private String password ;
    
    private String phone ;
}
