package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.Entity.AllowedArea;
import java.util.List;

public interface AllowedAreaService {
    List<AllowedArea> getAll();
    AllowedArea create(AllowedArea area);
    AllowedArea update(Long id, AllowedArea area);
    void delete(Long id);
    boolean isAllowed(String city, String district);
} 