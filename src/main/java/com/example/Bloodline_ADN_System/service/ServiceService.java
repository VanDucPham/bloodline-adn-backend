package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.Entity.Service;
import com.example.Bloodline_ADN_System.dto.noneWhere.ServiceDTO;

import java.util.List;
import java.util.Optional;

public interface ServiceService {
    List<ServiceDTO> getAllServices();
    Optional<Service> findServiceById(Long ServiceId);
    ServiceDTO getServiceById(Long id);
}
