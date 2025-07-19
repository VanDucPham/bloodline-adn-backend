package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.ConsultationRequest;
import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.dto.ConsultationRequestDTO;
import com.example.Bloodline_ADN_System.dto.CreateConsultationRequestDTO;
import com.example.Bloodline_ADN_System.repository.ConsultationRequestRepository;
import com.example.Bloodline_ADN_System.repository.UserRepository;
import com.example.Bloodline_ADN_System.service.ConsultationRequestService;
import com.example.Bloodline_ADN_System.utils.ConsultationValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsultationRequestServiceImpl implements ConsultationRequestService {

    @Autowired
    private ConsultationRequestRepository consultationRequestRepository;
    
    @Autowired
    private UserRepository userRepository;

    // ✅ Đăng ký tư vấn (Public)
    @Override
    public ConsultationRequestDTO createConsultationRequest(CreateConsultationRequestDTO request) {
        // Validation
        List<String> validationErrors = ConsultationValidation.validateConsultationRequest(request);
        if (!validationErrors.isEmpty()) {
            throw new RuntimeException("Validation errors: " + String.join(", ", validationErrors));
        }
        
        // Clean phone number
        String cleanPhone = request.getPhone().trim().replaceAll("[\\s\\-_()]", "");
        
        // Check if phone already exists
        List<ConsultationRequest> existingRequests = consultationRequestRepository.findByPhoneOrderByCreatedAtDesc(cleanPhone);
        if (!existingRequests.isEmpty()) {
            throw new RuntimeException("Số điện thoại này đã được đăng ký tư vấn trước đó");
        }
        
        ConsultationRequest consultationRequest = new ConsultationRequest();
        consultationRequest.setCustomerName(request.getCustomerName().trim());
        consultationRequest.setPhone(cleanPhone);
        consultationRequest.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        consultationRequest.setContent(request.getContent().trim());
        
        ConsultationRequest saved = consultationRequestRepository.save(consultationRequest);
        return convertToDTO(saved);
    }

    // ✅ Theo dõi trạng thái (Khách hàng)
    @Override
    public ConsultationRequestDTO getConsultationStatusByPhone(String phone) {
        List<ConsultationRequest> requests = consultationRequestRepository.findByPhoneOrderByCreatedAtDesc(phone);
        if (requests.isEmpty()) {
            throw new RuntimeException("Không tìm thấy yêu cầu tư vấn với số điện thoại: " + phone);
        }
        // Lấy bản ghi mới nhất
        ConsultationRequest request = requests.getFirst();
        return convertToDTO(request);
    }

    // ✅ Quản lý yêu cầu (Admin/Manager)
    @Override
    public Page<ConsultationRequestDTO> getAllConsultationRequests(Pageable pageable) {
        Page<ConsultationRequest> requests = consultationRequestRepository.findAll(pageable);
        return requests.map(this::convertToDTO);
    }

    @Override
    public ConsultationRequestDTO getConsultationRequestById(Long id) {
        ConsultationRequest request = consultationRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu tư vấn với ID: " + id));
        return convertToDTO(request);
    }

    @Override
    public Page<ConsultationRequestDTO> searchConsultationRequests(String keyword, Pageable pageable) {
        Page<ConsultationRequest> requests = consultationRequestRepository.findByFilters(null, null, keyword, pageable);
        return requests.map(this::convertToDTO);
    }

    @Override
    public Page<ConsultationRequestDTO> filterConsultationRequests(String status, Long staffId, String keyword, Pageable pageable) {
        ConsultationRequest.RequestStatus requestStatus = status != null ? 
            ConsultationRequest.RequestStatus.valueOf(status.toUpperCase()) : null;
        
        Page<ConsultationRequest> requests = consultationRequestRepository.findByFilters(requestStatus, staffId, keyword, pageable);
        return requests.map(this::convertToDTO);
    }

    // ✅ Cập nhật trạng thái (Admin/Manager/Staff)
    @Override
    public ConsultationRequestDTO updateStatus(Long id, String status) {
        ConsultationRequest request = consultationRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu tư vấn với ID: " + id));
        
        request.setStatus(ConsultationRequest.RequestStatus.valueOf(status.toUpperCase()));
        ConsultationRequest saved = consultationRequestRepository.save(request);
        return convertToDTO(saved);
    }

    // ✅ Phân công nhân viên (Manager)
    @Override
    public ConsultationRequestDTO assignStaff(Long requestId, Long staffId) {
        ConsultationRequest request = consultationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu tư vấn với ID: " + requestId));
        
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + staffId));
        
        request.setStaff(staff);
        request.setStatus(ConsultationRequest.RequestStatus.IN_PROGRESS);
        
        ConsultationRequest saved = consultationRequestRepository.save(request);
        return convertToDTO(saved);
    }

    // ✅ Xử lý tư vấn (Staff)
    @Override
    public List<ConsultationRequestDTO> getMyAssignments(Long staffId) {
        List<ConsultationRequest> requests = consultationRequestRepository.findByStaffUserId(staffId);
        return requests.stream().map(this::convertToDTO).toList();
    }

    @Override
    public ConsultationRequestDTO updateProgress(Long requestId, String progress) {
        ConsultationRequest request = consultationRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu tư vấn với ID: " + requestId));
        
        // Có thể thêm field progress vào entity nếu cần
        // request.setProgress(progress);
        
        ConsultationRequest saved = consultationRequestRepository.save(request);
        return convertToDTO(saved);
    }

    // ✅ Thống kê báo cáo (Admin)
    @Override
    public Map<String, Object> getConsultationStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalRequests = consultationRequestRepository.count();
        long newRequests = consultationRequestRepository.countByStatus(ConsultationRequest.RequestStatus.NEW);
        long inProgressRequests = consultationRequestRepository.countByStatus(ConsultationRequest.RequestStatus.IN_PROGRESS);
        long resolvedRequests = consultationRequestRepository.countByStatus(ConsultationRequest.RequestStatus.RESOLVED);
        long closedRequests = consultationRequestRepository.countByStatus(ConsultationRequest.RequestStatus.CLOSED);
        
        stats.put("total", totalRequests);
        stats.put("new", newRequests);
        stats.put("inProgress", inProgressRequests);
        stats.put("resolved", resolvedRequests);
        stats.put("closed", closedRequests);
        
        return stats;
    }

    @Override
    public Map<String, Object> getConsultationStatsByStaff() {
        Map<String, Object> stats = new HashMap<>();
        
        // Thống kê theo nhân viên
        List<User> staffList = userRepository.findAll().stream()
                .filter(user -> user.getRole().equals("STAFF") || user.getRole().equals("ADMIN"))
                .toList();
        
        for (User staff : staffList) {
            long count = consultationRequestRepository.countByStaffUserId(staff.getUserId());
            stats.put(staff.getName(), count);
        }
        
        return stats;
    }

    @Override
    public Map<String, Object> getConsultationStatsByDate(String dateFrom, String dateTo) {
        Map<String, Object> stats = new HashMap<>();
        
        LocalDateTime start = LocalDate.parse(dateFrom).atStartOfDay();
        LocalDateTime end = LocalDate.parse(dateTo).atTime(23, 59, 59);
        
        List<ConsultationRequest> requests = consultationRequestRepository.findByCreatedAtBetween(start, end);
        
        stats.put("totalInPeriod", requests.size());
        stats.put("dateFrom", dateFrom);
        stats.put("dateTo", dateTo);
        
        return stats;
    }

    // ✅ Lấy yêu cầu theo trạng thái
    @Override
    public List<ConsultationRequestDTO> getConsultationRequestsByStatus(String status) {
        ConsultationRequest.RequestStatus requestStatus = ConsultationRequest.RequestStatus.valueOf(status.toUpperCase());
        List<ConsultationRequest> requests = consultationRequestRepository.findByStatus(requestStatus);
        return requests.stream().map(this::convertToDTO).toList();
    }

    private ConsultationRequestDTO convertToDTO(ConsultationRequest request) {
        ConsultationRequestDTO dto = new ConsultationRequestDTO();
        dto.setRequestId(request.getRequestId());
        dto.setCustomerName(request.getCustomerName());
        dto.setPhone(request.getPhone());
        dto.setEmail(request.getEmail());
        dto.setContent(request.getContent());
        dto.setStatus(request.getStatus().toString());
        dto.setCreatedAt(request.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        
        if (request.getStaff() != null) {
            dto.setHandledBy(request.getStaff().getName());
            dto.setStaffId(request.getStaff().getUserId());
        }
        
        return dto;
    }
} 