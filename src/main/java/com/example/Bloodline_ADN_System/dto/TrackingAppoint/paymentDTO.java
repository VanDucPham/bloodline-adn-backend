package com.example.Bloodline_ADN_System.dto.TrackingAppoint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class paymentDTO {
    private Long paymentID;
    private String paymentStatus;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private Double paymentAmount;

}
