package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.Entity.AllowedArea;
import com.example.Bloodline_ADN_System.service.AllowedAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
public class AllowedAreaController {
    @Autowired
    private AllowedAreaService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public List<AllowedArea> getAll() {
        return service.getAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public AllowedArea create(@RequestBody AllowedArea area) {
        return service.create(area);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public AllowedArea update(@PathVariable Long id, @RequestBody AllowedArea area) {
        return service.update(id, area);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // API kiểm tra khu vực hợp lệ (chỉ cho phép CUSTOMER)
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/check")
    public boolean checkAllowed(@RequestParam String city, @RequestParam String district) {
        return service.isAllowed(city, district);
    }
} 