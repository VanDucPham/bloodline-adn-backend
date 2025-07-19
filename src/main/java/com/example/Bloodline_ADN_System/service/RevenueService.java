package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.RevenueDTO;

import java.time.LocalDateTime;
import java.util.Map;

public interface RevenueService {
    
    // Lấy thống kê tổng quan doanh thu
    RevenueDTO getRevenueOverview(LocalDateTime startDate, LocalDateTime endDate);
    
    // Lấy thống kê doanh thu theo tháng
    RevenueDTO getRevenueByMonth(Integer year, Integer month);
    
    // Lấy thống kê doanh thu theo service
    RevenueDTO getRevenueByService(Long serviceId, LocalDateTime startDate, LocalDateTime endDate);
    
    // Lấy thống kê payment method
    Map<String, Object> getPaymentMethodStats(LocalDateTime startDate, LocalDateTime endDate);
    
    // Lấy thống kê tăng trưởng doanh thu
    Map<String, Object> getRevenueGrowth(LocalDateTime startDate, LocalDateTime endDate);
    
    // Lấy top services theo doanh thu
    RevenueDTO getTopServicesByRevenue(LocalDateTime startDate, LocalDateTime endDate, int limit);
    
    // Lấy dữ liệu mẫu cho testing
    RevenueDTO getSampleRevenueData();
} 