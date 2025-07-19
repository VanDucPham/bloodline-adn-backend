package com.example.Bloodline_ADN_System.dto.managerCaseFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class caseFileDTO {
    private String caseCode;
    private String caseType;
    private String status;
    private Long serviceId;
    private Long userId;
}

