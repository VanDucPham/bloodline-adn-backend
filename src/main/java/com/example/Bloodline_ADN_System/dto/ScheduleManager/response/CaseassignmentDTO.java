package com.example.Bloodline_ADN_System.dto.ScheduleManager.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CaseassignmentDTO {
    private Long  appointmentID;
    private String caseCode ;
    private String customerName ;
    private String assignStaff ;
    private String appointmentType ;
    private String deliveryMethod ;
    public CaseassignmentDTO() {

    }
}

