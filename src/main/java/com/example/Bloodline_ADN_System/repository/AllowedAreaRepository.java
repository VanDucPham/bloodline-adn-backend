package com.example.Bloodline_ADN_System.repository;

import com.example.Bloodline_ADN_System.Entity.AllowedArea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AllowedAreaRepository extends JpaRepository<AllowedArea, Long> {
    List<AllowedArea> findByActiveTrue();
    List<AllowedArea> findByCityAndDistrictAndActiveTrue(String city, String district);
} 