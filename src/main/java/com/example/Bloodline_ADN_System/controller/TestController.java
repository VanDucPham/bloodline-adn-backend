package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.config.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private DataInitializer dataInitializer;

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Application is running");
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "Bloodline ADN System");
        response.put("version", "1.0.0");
        return response;
    }

    @GetMapping("/init-data")
    public Map<String, Object> initData() {
        Map<String, Object> response = new HashMap<>();
        try {
            dataInitializer.initializeData();
            response.put("status", "SUCCESS");
            response.put("message", "Dữ liệu mẫu đã được tạo thành công!");
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Lỗi khi tạo dữ liệu: " + e.getMessage());
        }
        return response;
    }
} 