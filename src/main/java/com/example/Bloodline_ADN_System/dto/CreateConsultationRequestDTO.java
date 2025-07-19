package com.example.Bloodline_ADN_System.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateConsultationRequestDTO {
    private String customerName;
    private String phone;
    private String email;
    private String content;
} 