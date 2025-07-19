package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.ManagerService.ServiceManagerDTO;

import java.util.List;

public interface ServiceList {
    List<ServiceManagerDTO> getAllServices();
    ServiceManagerDTO getServiceById(Long id);
    ServiceManagerDTO createService(ServiceManagerDTO dto);
    ServiceManagerDTO updateService(Long id, ServiceManagerDTO dto);
    void deleteService(Long id);
}