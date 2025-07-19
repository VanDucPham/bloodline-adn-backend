package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.TrackingAppoint.AppointmentResponseDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentDTO;

import java.util.List;

public interface CustomerService {

    List<AppointmentResponseDTO> getAllAppointments(Long customer_id);

    void updateAppointment(AppointmentDTO appointmentDTO, Long appointment_id);




}
