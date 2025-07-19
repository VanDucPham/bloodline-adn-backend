package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.CaseTrackingDTO;

import java.util.List;

public interface CaseTrackingService {
    List<CaseTrackingDTO> getAllCaseTracking();
    CaseTrackingDTO getCaseTrackingByCaseCode(String caseCode);
}
