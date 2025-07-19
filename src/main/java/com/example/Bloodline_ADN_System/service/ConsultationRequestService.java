package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.ConsultationRequestDTO;
import com.example.Bloodline_ADN_System.dto.CreateConsultationRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ConsultationRequestService {
    
    // ✅ Đăng ký tư vấn (Public)
    ConsultationRequestDTO createConsultationRequest(CreateConsultationRequestDTO request);
    
    // ✅ Theo dõi trạng thái (Khách hàng)
    ConsultationRequestDTO getConsultationStatusByPhone(String phone);
    
    // ✅ Quản lý yêu cầu (Admin/Manager)
    Page<ConsultationRequestDTO> getAllConsultationRequests(Pageable pageable);
    ConsultationRequestDTO getConsultationRequestById(Long id);
    Page<ConsultationRequestDTO> searchConsultationRequests(String keyword, Pageable pageable);
    Page<ConsultationRequestDTO> filterConsultationRequests(String status, Long staffId, String keyword, Pageable pageable);
    
    // ✅ Cập nhật trạng thái (Admin/Manager/Staff)
    ConsultationRequestDTO updateStatus(Long id, String status);
    
    // ✅ Phân công nhân viên (Manager)
    ConsultationRequestDTO assignStaff(Long requestId, Long staffId);
    
    // ✅ Xử lý tư vấn (Staff)
    List<ConsultationRequestDTO> getMyAssignments(Long staffId);
    ConsultationRequestDTO updateProgress(Long requestId, String progress);
    
    // ✅ Thống kê báo cáo (Admin)
    Map<String, Object> getConsultationStats();
    Map<String, Object> getConsultationStatsByStaff();
    Map<String, Object> getConsultationStatsByDate(String dateFrom, String dateTo);
    
    // ✅ Lấy yêu cầu theo trạng thái
    List<ConsultationRequestDTO> getConsultationRequestsByStatus(String status);
} 