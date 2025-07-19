package com.example.Bloodline_ADN_System.dto.ManagerService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceManagerDTO {
    private Long serviceId;
    private String serviceName;
    private String serviceDescription;
    private Integer limitPeople;
    private Double servicePrice;
    private String imageUrl;
}
