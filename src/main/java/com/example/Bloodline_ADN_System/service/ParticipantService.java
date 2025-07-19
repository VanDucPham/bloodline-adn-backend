package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.Entity.Participant;
import com.example.Bloodline_ADN_System.dto.noneWhere.ParticipantResponeDTO;
import com.example.Bloodline_ADN_System.dto.TrackingAppoint.UpdateParticipant;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.ParticipantDTO;

import java.util.List;

public interface ParticipantService {


    ///
    List<ParticipantDTO> addParticipant(List<ParticipantDTO> participantDTOList);
    List<ParticipantResponeDTO> getParticipantByAppointmentId(Long appointmentId);

    List<Participant> saveAll(List<Participant> participants);
    /// / old của Đạt
    public boolean updateParticipant(Long id,UpdateParticipant participant);



}