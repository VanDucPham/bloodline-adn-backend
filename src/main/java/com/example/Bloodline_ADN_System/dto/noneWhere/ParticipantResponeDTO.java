package com.example.Bloodline_ADN_System.dto.noneWhere;

import com.example.Bloodline_ADN_System.Entity.Participant;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ParticipantResponeDTO {
    private Long participantId;
    private String name;
    private String relationship;
    private Participant.Gender gender;
    private String citizenId;
    private String address;
    private LocalDate birthDate;
    private Long appointmentId;
    private SampleDTO sampleDTO;
}
