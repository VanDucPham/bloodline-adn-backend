package com.example.Bloodline_ADN_System.dto.ScheduleManager.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayscheduleDTO {
    private LocalDate day;
    private List<ShiftDTO> cases;


}
