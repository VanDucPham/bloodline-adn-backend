package com.example.Bloodline_ADN_System.dto.noneWhere;

import lombok.AllArgsConstructor;
import lombok.Data;



import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDTO {
    private String serviceName;
    private String serviceDescription;
    private Long serviceId;
    private Integer Limit_people ;
    private Double servicePrice ;
    private String imageUrl;

    public ServiceDTO(String serviceName, Integer limitPeople, String serviceDescription, Long serviceId, Double servicePrice, String imageUrl) {
        this.serviceName = serviceName;
        this.Limit_people = limitPeople;
        this.serviceDescription = serviceDescription;
        this.serviceId = serviceId;
        this.servicePrice = servicePrice;
        this.imageUrl = imageUrl;
    }


}

