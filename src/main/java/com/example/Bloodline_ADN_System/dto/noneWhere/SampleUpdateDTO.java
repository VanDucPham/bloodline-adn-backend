package com.example.Bloodline_ADN_System.dto.noneWhere;

import com.example.Bloodline_ADN_System.Entity.Sample;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
public class SampleUpdateDTO {
    private Long sampleId;
    private Sample.SampleQuality quality;
    private Sample.SampleStatus status;
    private String result;
    private String notes;
}