package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.dto.ConsultationRequestDTO;
import com.example.Bloodline_ADN_System.dto.CreateConsultationRequestDTO;
import com.example.Bloodline_ADN_System.service.ConsultationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultation")
@CrossOrigin(origins = "*")
public class ConsultationRequestController {

    @Autowired
    private ConsultationRequestService consultationRequestService;

    // ✅ Đăng ký tư vấn (Public) - Khách hàng
    @PostMapping("/register")
    public ResponseEntity<?> createConsultationRequest(@RequestBody CreateConsultationRequestDTO request) {
        try {
            ConsultationRequestDTO created = consultationRequestService.createConsultationRequest(request);
            
            // Thông báo thành công
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Đăng ký tư vấn thành công! Chúng tôi sẽ liên hệ với bạn trong vòng 30 phút.");
            response.put("data", created);
            response.put("requestId", created.getRequestId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Lỗi khi tạo yêu cầu tư vấn: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // ✅ Theo dõi trạng thái (Khách hàng)
    @GetMapping("/status/{phone}")
    public ResponseEntity<?> getConsultationStatusByPhone(@PathVariable String phone) {
        try {
            ConsultationRequestDTO request = consultationRequestService.getConsultationStatusByPhone(phone);
            
            // Thông báo thành công với thông tin chi tiết
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Tìm thấy yêu cầu tư vấn");
            response.put("data", request);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Không tìm thấy yêu cầu tư vấn: " + e.getMessage());
            
            return ResponseEntity.status(404).body(errorResponse);
        }
    }


} 