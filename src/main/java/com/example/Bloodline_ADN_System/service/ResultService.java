package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.ResultDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;

public interface ResultService {
    ResultDTO getResultByAppointmentId(Long appointmentId);
    String validateAppointmentForResult(Long appointmentId);
    ResultDTO createResult(ResultDTO dto);
    ResponseEntity<ByteArrayResource> exportResultPdf(Long appointmentId);
}
