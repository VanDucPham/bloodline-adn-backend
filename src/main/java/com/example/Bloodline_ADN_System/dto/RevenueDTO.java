package com.example.Bloodline_ADN_System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDTO {
    private Double totalRevenue;
    private Long totalCases;
    private Double averageRevenuePerCase;
    private Double growthPercent;
    private Double maxRevenueMonth;
    private Double minRevenueMonth;
    private List<MonthlyRevenueDTO> monthlyData;
    private List<ServiceRevenueDTO> serviceRevenue;
    private Map<String, Object> paymentMethodStats;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
} 