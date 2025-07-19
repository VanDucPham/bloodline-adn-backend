package com.example.Bloodline_ADN_System.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRequestDTO {
    private Long requestId;
    private String customerName;
    private String phone;
    private String email;
    private String content;
    private String status;
    private String createdAt;
    private String handledBy; // Tên nhân viên xử lý
    private Long staffId; // ID nhân viên xử lý
} 