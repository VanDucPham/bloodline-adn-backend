package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.dto.noneWhere.SampleDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentResponse;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.ParticipantDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.SampleCustomerDTO;
import com.example.Bloodline_ADN_System.service.AppointmentService;
import com.example.Bloodline_ADN_System.service.ParticipantService;
import com.example.Bloodline_ADN_System.service.impl.SampleServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer/caseFile")
public class CaseFileController {
    private final SampleServiceImpl sampleServiceImpl;
    private AppointmentService appointmentService;
    private final ParticipantService participantService;

    public CaseFileController(AppointmentService appointmentService, ParticipantService participantService, SampleServiceImpl sampleServiceImpl) {
        this.appointmentService = appointmentService;
        this.participantService = participantService;
        this.sampleServiceImpl = sampleServiceImpl ;
    }


    @PostMapping("/ sample")
    public ResponseEntity<List<SampleDTO>> createSamplesByUser(@RequestBody List<SampleCustomerDTO> dtoList) {
        System.out.println("hello");
        return ResponseEntity.ok(sampleServiceImpl.createSamplesByCustomer(dtoList));
    }

    @PostMapping("/apppointment")
    public ResponseEntity<AppointmentResponse<AppointmentDTO>> createAppointment(@RequestBody AppointmentDTO dto) {
        AppointmentResponse<AppointmentDTO> response = appointmentService.createAppointmentCaseFile(dto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/paticipant")
    public ResponseEntity<List<ParticipantDTO>> addParticipants(@RequestBody List<ParticipantDTO> participantDTOs) {
        List<ParticipantDTO> savedParticipants = participantService.addParticipant(participantDTOs);
        return ResponseEntity.ok(savedParticipants);
    }

}
