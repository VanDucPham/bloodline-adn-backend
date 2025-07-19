package com.example.Bloodline_ADN_System.dto.noneWhere;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentCheckRequest {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
}
