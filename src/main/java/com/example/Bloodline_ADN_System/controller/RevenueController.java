package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.dto.RevenueDTO;
import com.example.Bloodline_ADN_System.service.RevenueService;
import com.example.Bloodline_ADN_System.service.impl.RevenueServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/revenue")
public class RevenueController {

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping("/overview")
    public ResponseEntity<RevenueDTO> getRevenueOverview(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        // Nếu không có startDate và endDate, lấy dữ liệu của tháng hiện tại
        if (startDate == null || endDate == null) {
            LocalDateTime now = LocalDateTime.now();
            startDate = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            endDate = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
        }
        
        RevenueDTO revenueDTO = revenueService.getRevenueOverview(startDate, endDate);
        return ResponseEntity.ok(revenueDTO);
    }

    @GetMapping("/monthly")
    public ResponseEntity<RevenueDTO> getRevenueByMonth(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        
        RevenueDTO revenueDTO = revenueService.getRevenueByMonth(year, month);
        return ResponseEntity.ok(revenueDTO);
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<RevenueDTO> getRevenueByService(
            @PathVariable Long serviceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        // Nếu không có startDate và endDate, lấy dữ liệu của tháng hiện tại
        if (startDate == null || endDate == null) {
            LocalDateTime now = LocalDateTime.now();
            startDate = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            endDate = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
        }
        
        RevenueDTO revenueDTO = revenueService.getRevenueByService(serviceId, startDate, endDate);
        return ResponseEntity.ok(revenueDTO);
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<Map<String, Object>> getPaymentMethodStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        // Nếu không có startDate và endDate, lấy dữ liệu của tháng hiện tại
        if (startDate == null || endDate == null) {
            LocalDateTime now = LocalDateTime.now();
            startDate = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            endDate = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
        }
        
        Map<String, Object> stats = revenueService.getPaymentMethodStats(startDate, endDate);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/growth")
    public ResponseEntity<Map<String, Object>> getRevenueGrowth(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        // Nếu không có startDate và endDate, lấy dữ liệu của tháng hiện tại
        if (startDate == null || endDate == null) {
            LocalDateTime now = LocalDateTime.now();
            startDate = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            endDate = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
        }
        
        Map<String, Object> growth = revenueService.getRevenueGrowth(startDate, endDate);
        return ResponseEntity.ok(growth);
    }

    @GetMapping("/top-services")
    public ResponseEntity<RevenueDTO> getTopServicesByRevenue(
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        // Nếu không có startDate và endDate, lấy dữ liệu của tháng hiện tại
        if (startDate == null || endDate == null) {
            LocalDateTime now = LocalDateTime.now();
            startDate = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            endDate = now.withDayOfMonth(now.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59);
        }
        
        RevenueDTO revenueDTO = revenueService.getTopServicesByRevenue(startDate, endDate, limit);
        return ResponseEntity.ok(revenueDTO);
    }

    // Endpoint để lấy dữ liệu mẫu cho testing
    @GetMapping("/sample")
    public ResponseEntity<RevenueDTO> getSampleRevenueData() {
        RevenueDTO revenueDTO = revenueService.getSampleRevenueData();
        return ResponseEntity.ok(revenueDTO);
    }
} 