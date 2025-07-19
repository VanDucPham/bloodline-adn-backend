package com.example.Bloodline_ADN_System.dto.ScheduleManager.request;

import lombok.Data;

import java.util.List;
@Data

public class AssignmentRequestDTO {
    List<AppoinmentAsignedStaffDTO> appoinment;
}
