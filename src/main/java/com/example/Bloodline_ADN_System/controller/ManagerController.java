package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.dto.CaseTrackingDTO;
import com.example.Bloodline_ADN_System.service.impl.CaseTrackingServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/manager")
@AllArgsConstructor
public class ManagerController {
    private final CaseTrackingServiceImpl caseTrackingService;

    @GetMapping("/tracking")
    public List<CaseTrackingDTO> getAllTrackedCases() {
        return caseTrackingService.getAllCaseTracking();
    }

    @GetMapping("/tracking/{caseId}")
    public CaseTrackingDTO getCaseById(@PathVariable String caseId) {
        return caseTrackingService.getCaseTrackingByCaseCode(caseId);
    }
}
