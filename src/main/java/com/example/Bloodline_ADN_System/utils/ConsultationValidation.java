package com.example.Bloodline_ADN_System.utils;

import com.example.Bloodline_ADN_System.dto.CreateConsultationRequestDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ConsultationValidation {
    
    public static List<String> validateConsultationRequest(CreateConsultationRequestDTO request) {
        List<String> errors = new ArrayList<>();
        
        // Validate customerName
        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
            errors.add("Tên khách hàng không được để trống");
        } else if (request.getCustomerName().trim().length() < 2) {
            errors.add("Tên khách hàng phải có ít nhất 2 ký tự");
        } else if (request.getCustomerName().trim().length() > 100) {
            errors.add("Tên khách hàng không được quá 100 ký tự");
        }
        
        // Validate phone
        if (request.getPhone() == null || request.getPhone().trim().isEmpty()) {
            errors.add("Số điện thoại không được để trống");
        } else {
            String phone = request.getPhone().trim();
            // Loại bỏ tất cả khoảng trắng và ký tự đặc biệt
            phone = phone.replaceAll("[\\s\\-_()]", "");
            
            // Regex cho số điện thoại Việt Nam
            String phoneRegex = "^(0|\\+84|84)[0-9]{9}$";
            if (!Pattern.matches(phoneRegex, phone)) {
                errors.add("Số điện thoại không hợp lệ (định dạng: 0123456789, +84123456789 hoặc 84123456789)");
            }
        }
        
        // Validate email (optional but if provided must be valid)
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!Pattern.matches(emailRegex, request.getEmail().trim())) {
                errors.add("Email không hợp lệ");
            } else if (request.getEmail().trim().length() > 100) {
                errors.add("Email không được quá 100 ký tự");
            }
        }
        
        // Validate content
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            errors.add("Nội dung tư vấn không được để trống");
        } else if (request.getContent().trim().length() < 10) {
            errors.add("Nội dung tư vấn phải có ít nhất 10 ký tự");
        } else if (request.getContent().trim().length() > 1000) {
            errors.add("Nội dung tư vấn không được quá 1000 ký tự");
        }
        
        return errors;
    }
} 