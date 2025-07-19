package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import com.example.Bloodline_ADN_System.dto.ManagerService.ServiceManagerDTO;
import com.example.Bloodline_ADN_System.dto.PaymentDTO;
import com.example.Bloodline_ADN_System.dto.ResultDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentRequest;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentResponse;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.ParticipantDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.*;
import com.example.Bloodline_ADN_System.payment.PaymentRequest;
import com.example.Bloodline_ADN_System.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff")
public class StaffCaseFileController {
    private final AppointmentService appointmentService;
    private final SampleService sampleService;
    private final ParticipantService participantService;
    private final ServiceList serviceList;
    private final UserService userService;
    private final ResultService resultService;
    private final PaymentService paymentService;
    @GetMapping("/appointment")
    public ResponseEntity<List<AppointmentDTO>> filterAppointments(
            @RequestParam(required = false) Appointment.AppointmentStatus status,
            @RequestParam(required = false) Appointment.AppointmentType type,
            @RequestParam(name = "appointmentDate",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<AppointmentDTO> appointments = appointmentService.filterAppointment(status, type, date);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointment/{id}")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        AppointmentDTO appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/service/{id}")
    public ResponseEntity<ServiceManagerDTO> getServiceById(@PathVariable Long id) {
        ServiceManagerDTO service = serviceList.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @PostMapping("/sample/offline")
    public ResponseEntity<List<SampleDTO>> createSample(@RequestBody List<SampleStaffDTO> dtoList) {
        return ResponseEntity.ok(sampleService.createSampleByStaff(dtoList));
    }

    @PutMapping("/sample/update")
    public ResponseEntity<SampleDTO> updateSample(@RequestBody SampleUpdateDTO dto) throws JsonProcessingException {
        return ResponseEntity.ok(sampleService.updateSampleInfo(dto));
    }

    @GetMapping("/participant/{id}")
    public ResponseEntity<List<ParticipantResponeDTO>> getParticipantsByAppointment(@PathVariable("id") Long id) {
        List<ParticipantResponeDTO> list = participantService.getParticipantByAppointmentId(id);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/apointment/update/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @RequestParam(value = "status", required = false) Appointment.AppointmentStatus status,
                                                            @RequestParam(value = "collectionStatus", required = false) Appointment.CollectionStatus collectionStatus) {
        return ResponseEntity.ok(appointmentService.updateAppointmentProgress(id, status, collectionStatus));
    }

    @GetMapping("/sample/get/{id}")
    public ResponseEntity<SampleDTO> getSampleByParticipant(@PathVariable Long id){
        SampleDTO sample = sampleService.getSampleByParticipantId(id);
        return ResponseEntity.ok(sample);
    }

    @PostMapping("/appointment/create")
    public ResponseEntity<AppointmentResponse<AppointmentDTO>> createAppointment(
            @RequestBody AppointmentRequest request) {
        AppointmentResponse<AppointmentDTO> response = appointmentService.createAppointmentByStaff(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/services")
    public List<ServiceManagerDTO> getAllService() {
        return serviceList.getAllServices();
    }

    @GetMapping ("/profile")
    public ResponseEntity<UserUpdateDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping("/participant/create")
    public ResponseEntity<List<ParticipantDTO>> addParticipants(@RequestBody List<ParticipantDTO> participantDTOs) {
        List<ParticipantDTO> savedParticipants = participantService.addParticipant(participantDTOs);
        return ResponseEntity.ok(savedParticipants);
    }

    @PostMapping("/result/create")
    public ResultDTO createResult(@RequestBody ResultDTO resultDTO){
        resultService.createResult(resultDTO);
        return resultDTO;
    }

    @GetMapping("/result/get/{appointmentId}")
    public ResultDTO getResult(@PathVariable Long appointmentId){
        return resultService.getResultByAppointmentId(appointmentId);
    }

    @GetMapping("/export-pdf/{appointmentId}")
    public ResponseEntity<ByteArrayResource> exportPdf(@PathVariable Long appointmentId) {
        return resultService.exportResultPdf(appointmentId);
    }

    @PostMapping("/payment/{appointmentId}")
    public ResponseEntity<PaymentDTO> createPayment(@PathVariable Long appointmentId,@RequestBody PaymentRequest paymentRequest) {
        return ResponseEntity.ok(paymentService.createPaymentAndReturnDTO(paymentRequest, appointmentId));
    }
}
