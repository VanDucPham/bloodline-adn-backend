package com.example.Bloodline_ADN_System.dto.noneWhere;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackResponse {
    private Long feedbackId;
    private Long userId;
    private Long serviceId;
    private Long appointmentId;
    private String feedbackText;
    private Integer rating;
    private LocalDateTime feedbackDate;
}
