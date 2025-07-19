package com.example.Bloodline_ADN_System.dto.managerCaseFile;

import com.example.Bloodline_ADN_System.Entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class caseFileResponse {
    private Long id;
    private String caseCode;
    private String caseType;
    private String status;
    private Long serviceId;
    private String serviceName;
    private User createdBy;
    private LocalDateTime createdAt;
}
