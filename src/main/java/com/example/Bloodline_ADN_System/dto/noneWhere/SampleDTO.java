package com.example.Bloodline_ADN_System.dto.noneWhere;

import com.example.Bloodline_ADN_System.Entity.Sample;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SampleDTO {
    private Long sampleId;
    private Long participantId;
    private String participantCitizenId ;
    private String participantName;
    private String gender ;
    private String sampleType;
    private LocalDateTime collectionDateTime;
    private Sample.SampleQuality quality;
    private Sample.SampleStatus status;
    private String result;
    private String notes;
}
