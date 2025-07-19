package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.AllowedArea;
import com.example.Bloodline_ADN_System.repository.AllowedAreaRepository;
import com.example.Bloodline_ADN_System.service.AllowedAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllowedAreaServiceImpl implements AllowedAreaService {
    @Autowired
    private AllowedAreaRepository repo;

    @Override
    public List<AllowedArea> getAll() {
        return repo.findAll();
    }

    @Override
    public AllowedArea create(AllowedArea area) {
        return repo.save(area);
    }

    @Override
    public AllowedArea update(Long id, AllowedArea area) {
        AllowedArea old = repo.findById(id).orElseThrow();
        old.setCity(area.getCity());
        old.setDistrict(area.getDistrict());
        old.setActive(area.getActive());
        old.setNote(area.getNote());
        return repo.save(old);
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Override
    public boolean isAllowed(String city, String district) {
        return !repo.findByCityAndDistrictAndActiveTrue(city, district).isEmpty();
    }
} 