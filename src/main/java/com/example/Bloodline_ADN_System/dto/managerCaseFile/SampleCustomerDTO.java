package com.example.Bloodline_ADN_System.dto.managerCaseFile;

import com.example.Bloodline_ADN_System.Entity.Sample;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SampleCustomerDTO {
    private Long participantId;
    private String sampleType;
    private LocalDateTime collectionDateTime;
}
