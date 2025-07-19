package com.example.Bloodline_ADN_System.dto;

import com.example.Bloodline_ADN_System.Entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private Long appointmentId;
    private Double amount;
    private LocalDateTime paymentDate;
    private Payment.PaymentMethod paymentMethod;
    private Payment.PaymentStatus status;
    private String notes;
    
    // Thông tin liên quan
    private String serviceName;
    private String customerName;
    private String caseCode;
} 