package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.dto.RevenueDTO;
import com.example.Bloodline_ADN_System.dto.MonthlyRevenueDTO;
import com.example.Bloodline_ADN_System.dto.ServiceRevenueDTO;
import com.example.Bloodline_ADN_System.repository.PaymentRepository;
import com.example.Bloodline_ADN_System.repository.AppointmentRepository;
import com.example.Bloodline_ADN_System.service.RevenueService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class RevenueServiceImpl implements RevenueService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;

    public RevenueServiceImpl(PaymentRepository paymentRepository, AppointmentRepository appointmentRepository) {
        this.paymentRepository = paymentRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public RevenueDTO getRevenueOverview(LocalDateTime startDate, LocalDateTime endDate) {
        RevenueDTO revenueDTO = new RevenueDTO();
        
        // ========================================
        // 1. TÍNH TỔNG DOANH THU
        // ========================================
        // Công thức: Tổng doanh thu = SUM(amount) WHERE status = 'COMPLETED' AND paymentDate BETWEEN startDate AND endDate
        Double totalRevenue = paymentRepository.sumAmountByDateRange(startDate, endDate);
        revenueDTO.setTotalRevenue(totalRevenue != null ? totalRevenue : 0.0);
        
        // ========================================
        // 2. TÍNH SỐ LƯỢNG HỒ SƠ (CASES)
        // ========================================
        // Công thức: Đếm appointments có payment completed trong khoảng thời gian
        Long totalCases = paymentRepository.countCompletedPaymentsByDateRange(startDate, endDate);
        revenueDTO.setTotalCases(totalCases);
        
        // ========================================
        // 3. TÍNH DOANH THU TRUNG BÌNH/HỒ SƠ
        // ========================================
        // Công thức: Doanh thu trung bình = Tổng doanh thu / Số lượng hồ sơ
        if (totalCases > 0 && totalRevenue != null && totalRevenue > 0) {
            revenueDTO.setAverageRevenuePerCase(totalRevenue / totalCases);
        } else {
            revenueDTO.setAverageRevenuePerCase(0.0);
        }
        
        // ========================================
        // 4. TÍNH TĂNG TRƯỞNG DOANH THU
        // ========================================
        Map<String, Object> growthData = getRevenueGrowth(startDate, endDate);
        revenueDTO.setGrowthPercent((Double) growthData.get("growthPercent"));
        
        // ========================================
        // 5. TÍNH DOANH THU CAO NHẤT VÀ THẤP NHẤT
        // ========================================
        // Công thức: Lấy MAX và MIN từ doanh thu theo tháng, chỉ trong khoảng thời gian được chọn
        List<Object[]> monthlyData = paymentRepository.getRevenueByMonth();
        // Lọc các tháng nằm trong khoảng startDate, endDate
        List<Object[]> filteredMonthlyData = new ArrayList<>();
        if (monthlyData != null && !monthlyData.isEmpty() && startDate != null && endDate != null) {
            for (Object[] row : monthlyData) {
                int year = ((Number) row[0]).intValue();
                int month = ((Number) row[1]).intValue();
                // Tạo ngày đầu tháng và cuối tháng
                java.time.LocalDateTime firstDay = java.time.LocalDateTime.of(year, month, 1, 0, 0);
                java.time.LocalDateTime lastDay = firstDay.plusMonths(1).minusSeconds(1);
                // Nếu tháng này giao với khoảng thời gian được chọn
                if (!(lastDay.isBefore(startDate) || firstDay.isAfter(endDate))) {
                    filteredMonthlyData.add(row);
                }
            }
        }
        if (!filteredMonthlyData.isEmpty()) {
            Double maxRevenue = filteredMonthlyData.stream()
                    .mapToDouble(row -> ((Number) row[2]).doubleValue())
                    .max()
                    .orElse(0.0);
            Double minRevenue = filteredMonthlyData.stream()
                    .mapToDouble(row -> ((Number) row[2]).doubleValue())
                    .min()
                    .orElse(0.0);
            revenueDTO.setMaxRevenueMonth(maxRevenue);
            revenueDTO.setMinRevenueMonth(minRevenue);
        } else {
            revenueDTO.setMaxRevenueMonth(0.0);
            revenueDTO.setMinRevenueMonth(0.0);
        }
        // Lấy dữ liệu theo tháng
        if (monthlyData != null) {
            revenueDTO.setMonthlyData(convertToMonthlyRevenueDTO(monthlyData));
        }
        
        // Lấy dữ liệu theo service
        revenueDTO.setServiceRevenue(convertToServiceRevenueDTO(paymentRepository.getRevenueByService()));
        
        // Lấy thống kê payment method
        revenueDTO.setPaymentMethodStats(getPaymentMethodStats(startDate, endDate));
        
        revenueDTO.setStartDate(startDate);
        revenueDTO.setEndDate(endDate);
        
        return revenueDTO;
    }

    @Override
    public RevenueDTO getRevenueByMonth(Integer year, Integer month) {
        // Tạo startDate và endDate cho tháng cụ thể
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);
        
        return getRevenueOverview(startDate, endDate);
    }

    @Override
    public RevenueDTO getRevenueByService(Long serviceId, LocalDateTime startDate, LocalDateTime endDate) {
        RevenueDTO revenueDTO = new RevenueDTO();
        
        // ========================================
        // TÍNH DOANH THU THEO SERVICE
        // ========================================
        // Công thức: SUM(amount) WHERE serviceId = :serviceId AND status = 'COMPLETED' AND paymentDate BETWEEN startDate AND endDate
        Double serviceRevenue = paymentRepository.sumAmountByServiceAndDateRange(serviceId, startDate, endDate);
        revenueDTO.setTotalRevenue(serviceRevenue != null ? serviceRevenue : 0.0);
        
        // Đếm số lượng cases cho service này
        Long serviceCases = paymentRepository.countCompletedPaymentsByServiceAndDateRange(serviceId, startDate, endDate);
        revenueDTO.setTotalCases(serviceCases);
        
        // Tính doanh thu trung bình per case cho service
        if (serviceCases > 0 && serviceRevenue != null && serviceRevenue > 0) {
            revenueDTO.setAverageRevenuePerCase(serviceRevenue / serviceCases);
        } else {
            revenueDTO.setAverageRevenuePerCase(0.0);
        }
        
        revenueDTO.setStartDate(startDate);
        revenueDTO.setEndDate(endDate);
        
        return revenueDTO;
    }

    @Override
    public Map<String, Object> getPaymentMethodStats(LocalDateTime startDate, LocalDateTime endDate) {
        // ========================================
        // THỐNG KÊ PHƯƠNG THỨC THANH TOÁN
        // ========================================
        // Công thức: GROUP BY paymentMethod, COUNT(*), SUM(amount) WHERE status = 'COMPLETED' AND paymentDate BETWEEN startDate AND endDate
        List<Object[]> stats = paymentRepository.getPaymentMethodStatsByDateRange(startDate, endDate);
        Map<String, Object> result = new HashMap<>();
        
        for (Object[] stat : stats) {
            String method = stat[0].toString();
            Long count = ((Number) stat[1]).longValue();
            Double total = ((Number) stat[2]).doubleValue();
            
            Map<String, Object> methodData = new HashMap<>();
            methodData.put("count", count);
            methodData.put("total", total);
            methodData.put("percentage", calculatePercentage(total, stats));
            result.put(method, methodData);
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getRevenueGrowth(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> result = new HashMap<>();
        
        // ========================================
        // TÍNH TĂNG TRƯỞNG DOANH THU
        // ========================================
        // Công thức tăng trưởng: ((Doanh thu hiện tại - Doanh thu trước) / Doanh thu trước) * 100
        
        // Tính doanh thu hiện tại
        Double currentRevenue = paymentRepository.sumAmountByDateRange(startDate, endDate);
        
        // Tính doanh thu tháng trước (cùng khoảng thời gian)
        LocalDateTime previousStart = startDate.minusMonths(1);
        LocalDateTime previousEnd = endDate.minusMonths(1);
        Double previousRevenue = paymentRepository.sumAmountByDateRange(previousStart, previousEnd);
        
        // Tính phần trăm tăng trưởng
        double growthPercent = 0.0;
        if (previousRevenue != null && previousRevenue > 0 && currentRevenue != null) {
            growthPercent = ((currentRevenue - previousRevenue) / previousRevenue) * 100;
        }
        
        // Tính tốc độ tăng trưởng (tăng/giảm tuyệt đối)
        Double absoluteGrowth = (currentRevenue != null ? currentRevenue : 0.0) - (previousRevenue != null ? previousRevenue : 0.0);
        
        result.put("currentRevenue", currentRevenue != null ? currentRevenue : 0.0);
        result.put("previousRevenue", previousRevenue != null ? previousRevenue : 0.0);
        result.put("growthPercent", growthPercent);
        result.put("absoluteGrowth", absoluteGrowth);
        result.put("growthType", growthPercent >= 0 ? "INCREASE" : "DECREASE");
        
        return result;
    }

    @Override
    public RevenueDTO getTopServicesByRevenue(LocalDateTime startDate, LocalDateTime endDate, int limit) {
        RevenueDTO revenueDTO = new RevenueDTO();
        
        // ========================================
        // TOP SERVICES THEO DOANH THU
        // ========================================
        // Công thức: GROUP BY serviceId, SUM(amount) ORDER BY SUM(amount) DESC LIMIT limit
        List<Object[]> serviceRevenue = paymentRepository.getTopServicesByRevenue(startDate, endDate);
        List<ServiceRevenueDTO> topServices = convertToServiceRevenueDTO(serviceRevenue)
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
        
        revenueDTO.setServiceRevenue(topServices);
        revenueDTO.setStartDate(startDate);
        revenueDTO.setEndDate(endDate);
        
        return revenueDTO;
    }

    // Helper methods
    private List<MonthlyRevenueDTO> convertToMonthlyRevenueDTO(List<Object[]> monthlyData) {
        return monthlyData.stream().map(row -> {
            MonthlyRevenueDTO dto = new MonthlyRevenueDTO();
            dto.setYear(((Number) row[0]).intValue());
            dto.setMonth(((Number) row[1]).intValue());
            dto.setRevenue(((Number) row[2]).doubleValue());
            dto.setCases(0L); // Tạm thời set 0, có thể tính sau
            return dto;
        }).collect(Collectors.toList());
    }

    private List<ServiceRevenueDTO> convertToServiceRevenueDTO(List<Object[]> serviceData) {
        return serviceData.stream().map(row -> {
            ServiceRevenueDTO dto = new ServiceRevenueDTO();
            dto.setServiceName((String) row[0]);
            dto.setRevenue(((Number) row[1]).doubleValue());
            dto.setCases(0L); // Tạm thời set 0, có thể tính sau
            return dto;
        }).collect(Collectors.toList());
    }

    // Helper method để tính phần trăm
    private Double calculatePercentage(Double value, List<Object[]> allStats) {
        Double total = allStats.stream()
                .mapToDouble(stat -> ((Number) stat[2]).doubleValue())
                .sum();
        
        if (total > 0) {
            return (value / total) * 100;
        }
        return 0.0;
    }

    // Method để tạo dữ liệu mẫu cho testing (có thể xóa sau)
    public RevenueDTO getSampleRevenueData() {
        RevenueDTO revenueDTO = new RevenueDTO();
        
        // Dữ liệu mẫu
        revenueDTO.setTotalRevenue(50000000.0);
        revenueDTO.setTotalCases(150L);
        revenueDTO.setAverageRevenuePerCase(333333.33);
        revenueDTO.setGrowthPercent(15.5);
        revenueDTO.setMaxRevenueMonth(8000000.0);
        revenueDTO.setMinRevenueMonth(3000000.0);
        
        // Dữ liệu theo tháng
        List<MonthlyRevenueDTO> monthlyData = Arrays.asList(
            createMonthlyRevenueDTO(2024, 1, 3500000.0, 15L),
            createMonthlyRevenueDTO(2024, 2, 4200000.0, 18L),
            createMonthlyRevenueDTO(2024, 3, 3800000.0, 16L),
            createMonthlyRevenueDTO(2024, 4, 4500000.0, 22L),
            createMonthlyRevenueDTO(2024, 5, 5000000.0, 25L)
        );
        revenueDTO.setMonthlyData(monthlyData);
        
        // Dữ liệu theo service
        List<ServiceRevenueDTO> serviceData = Arrays.asList(
            createServiceRevenueDTO("Xét nghiệm ADN huyết thống", 25000000.0, 75L),
            createServiceRevenueDTO("Xét nghiệm ADN cha con", 15000000.0, 45L),
            createServiceRevenueDTO("Xét nghiệm ADN mẹ con", 10000000.0, 30L)
        );
        revenueDTO.setServiceRevenue(serviceData);
        
        // Thống kê payment method
        Map<String, Object> paymentStats = new HashMap<>();
        
        Map<String, Object> creditCardData = new HashMap<>();
        creditCardData.put("count", 60L);
        creditCardData.put("total", 30000000.0);
        creditCardData.put("percentage", 60.0);
        paymentStats.put("CREDIT_CARD", creditCardData);
        
        Map<String, Object> bankTransferData = new HashMap<>();
        bankTransferData.put("count", 45L);
        bankTransferData.put("total", 15000000.0);
        bankTransferData.put("percentage", 30.0);
        paymentStats.put("BANK_TRANSFER", bankTransferData);
        
        Map<String, Object> cashData = new HashMap<>();
        cashData.put("count", 30L);
        cashData.put("total", 5000000.0);
        cashData.put("percentage", 10.0);
        paymentStats.put("CASH", cashData);
        
        revenueDTO.setPaymentMethodStats(paymentStats);
        
        return revenueDTO;
    }

    // Helper methods để tạo DTO objects
    private MonthlyRevenueDTO createMonthlyRevenueDTO(Integer year, Integer month, Double revenue, Long cases) {
        MonthlyRevenueDTO dto = new MonthlyRevenueDTO();
        dto.setYear(year);
        dto.setMonth(month);
        dto.setRevenue(revenue);
        dto.setCases(cases);
        return dto;
    }

    private ServiceRevenueDTO createServiceRevenueDTO(String serviceName, Double revenue, Long cases) {
        ServiceRevenueDTO dto = new ServiceRevenueDTO();
        dto.setServiceName(serviceName);
        dto.setRevenue(revenue);
        dto.setCases(cases);
        return dto;
    }
} 