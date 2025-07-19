package com.example.Bloodline_ADN_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRevenueDTO {
    private String serviceName;
    private Double revenue;
    private Long cases;
} 