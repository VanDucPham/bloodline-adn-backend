package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.dto.managerCaseFile.ParticipantDTO;
import com.example.Bloodline_ADN_System.service.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping
    public ResponseEntity<List<ParticipantDTO>> addParticipants(@RequestBody List<ParticipantDTO> participantDTOs) {
        List<ParticipantDTO> savedParticipants = participantService.addParticipant(participantDTOs);
        return ResponseEntity.ok(savedParticipants);
    }
}