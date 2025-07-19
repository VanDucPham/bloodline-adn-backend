package com.example.Bloodline_ADN_System.dto;

import com.example.Bloodline_ADN_System.Entity.Result;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResultDTO {
    private Long resultId;
    private Long appointmentId;
    private String resultValue;
    private LocalDateTime resultDate;
    private String notes;
    private Result.ResultStatus status;
    private String appointmentType; // Loại lịch hẹn
    private List<String> participants; // Danh sách tên participant
}
