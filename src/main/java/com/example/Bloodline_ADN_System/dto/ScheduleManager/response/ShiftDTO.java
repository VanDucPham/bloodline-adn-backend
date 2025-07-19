package com.example.Bloodline_ADN_System.dto.ScheduleManager.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShiftDTO {
    private String time ;
    private String staff ;
    private List<CaseassignmentDTO> caseassignments ;


}
