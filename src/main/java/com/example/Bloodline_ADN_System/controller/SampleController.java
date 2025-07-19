package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.dto.managerCaseFile.SampleCustomerDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleStaffDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleUpdateDTO;
import com.example.Bloodline_ADN_System.service.SampleService;
import com.example.Bloodline_ADN_System.service.impl.SampleServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/sample")
public class SampleController {
    private final SampleService sampleService;
    public SampleController(SampleServiceImpl sampleService) {
        this.sampleService = sampleService;
    }
    @PostMapping("/customer")
    public ResponseEntity<List<SampleDTO>> createSamplesByUser(@RequestBody List<SampleCustomerDTO> dtoList) {
        System.out.println("hello");
        return ResponseEntity.ok(sampleService.createSamplesByCustomer(dtoList));
    }

    @PostMapping("/staff")
    public ResponseEntity<List<SampleDTO>> createSamplesByCustomer(@RequestBody List<SampleStaffDTO> dtoList){
        return ResponseEntity.ok(sampleService.createSampleByStaff(dtoList));
    }

    @PutMapping("/update-info")
    public ResponseEntity<SampleDTO> updateSampleInfo(@RequestBody SampleUpdateDTO dto) throws JsonProcessingException {
        return ResponseEntity.ok(sampleService.updateSampleInfo(dto));
    }
}