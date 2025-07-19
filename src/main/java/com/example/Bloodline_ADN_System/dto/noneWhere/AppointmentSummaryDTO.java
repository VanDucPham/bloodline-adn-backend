package com.example.Bloodline_ADN_System.dto.noneWhere;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentSummaryDTO {
    private Long appointmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Appointment.AppointmentStatus status;
    private Long serviceId;
    private Appointment.AppointmentType appointmentType;
}
