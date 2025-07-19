package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import com.example.Bloodline_ADN_System.dto.noneWhere.ApiMessResponse;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentRequest;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentResponse;
import jakarta.transaction.Transactional;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {

    AppointmentResponse<AppointmentDTO> createAppointmentCaseFile(AppointmentDTO dto);
    AppointmentResponse<AppointmentDTO> createAppointmentByStaff(AppointmentRequest request);

    // --------------------- CUSTOMER CREATE APPOINTMENT ---------------------


    // --------------------- CUSTOMER CREATE APPOINTMENT ---------------------
    @Transactional
    AppointmentResponse<AppointmentDTO> createAppointment(AppointmentRequest request);

    AppointmentDTO getAppointmentById(Long id);
    boolean isUserIdExist(Long userId);

    List<AppointmentDTO> getAllAppointment();
    List<AppointmentDTO> getAppointmentByUserId(Long userId);
    ApiMessResponse cancelAppointment(Long id);
    List<AppointmentDTO> filterAppointment(Appointment.AppointmentStatus status, Appointment.AppointmentType type, LocalDate date);
    AppointmentDTO updateAppointmentProgress(Long id, Appointment.AppointmentStatus status,Appointment.CollectionStatus collectionStatus);

  //  Long getUserIdByUsername(String username);



        boolean checkAvailability(LocalDate date, LocalTime time, String email);
        Long getUserIdByUsername(String username);


    Long findCaseIdByAppointmentId(Long id);

    void updateAppointment(AppointmentDTO appointmentDTO, Long appointmentId);
}



