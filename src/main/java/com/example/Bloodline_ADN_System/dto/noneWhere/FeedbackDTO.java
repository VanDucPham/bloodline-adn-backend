package com.example.Bloodline_ADN_System.dto.noneWhere;

import lombok.Data;

@Data
public class FeedbackDTO {
    private Long serviceId; // Sẽ được set tự động từ URL parameter
    private Long appointmentId; // Sẽ được set tự động từ URL parameter
    private String feedbackText;
    private Integer rating;
}
