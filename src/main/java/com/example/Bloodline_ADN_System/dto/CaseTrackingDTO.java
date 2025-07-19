package com.example.Bloodline_ADN_System.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaseTrackingDTO {
    private String caseId;
    private String customer;
    private String type;
    private LocalDate createdAt;
    private String status;
    private String staff;
    private List<TimelineStep> timeline;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TimelineStep {
        private String title;
        private String date; // YYYY-MM-DD string format
    }
}
