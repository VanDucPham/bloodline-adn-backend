package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.Entity.Sample;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.SampleCustomerDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleStaffDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleUpdateDTO;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface SampleService {
    List<SampleDTO> createSamplesByCustomer(List<SampleCustomerDTO> dtoList);
    List<SampleDTO> createSampleByStaff(List<SampleStaffDTO> dtoList);
    SampleDTO updateSampleInfo(SampleUpdateDTO dto) throws JsonProcessingException;
    SampleDTO getSampleByParticipantId(Long participantId);
    SampleDTO updateSample(SampleDTO dto);
    void saveAll(List<Sample> samples);
}