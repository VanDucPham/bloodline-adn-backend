package com.example.Bloodline_ADN_System.repository;

import com.example.Bloodline_ADN_System.Entity.ConsultationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultationRequestRepository extends JpaRepository<ConsultationRequest, Long> {
    
    // Tìm theo trạng thái
    List<ConsultationRequest> findByStatus(ConsultationRequest.RequestStatus status);
    
    // Tìm theo nhân viên xử lý
    List<ConsultationRequest> findByStaffUserId(Long staffId);
    
    // Tìm theo số điện thoại (lấy bản ghi mới nhất)
    @Query("SELECT cr FROM ConsultationRequest cr WHERE cr.phone = :phone ORDER BY cr.createdAt DESC")
    List<ConsultationRequest> findByPhoneOrderByCreatedAtDesc(@Param("phone") String phone);
    

    
    // Đếm số lượng theo trạng thái
    long countByStatus(ConsultationRequest.RequestStatus status);
    
    // Đếm số lượng theo nhân viên
    long countByStaffUserId(Long staffId);
    
    // Tìm kiếm theo thời gian
    List<ConsultationRequest> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Tìm kiếm theo nhiều điều kiện
    @Query("SELECT cr FROM ConsultationRequest cr WHERE " +
           "(:status IS NULL OR cr.status = :status) AND " +
           "(:staffId IS NULL OR cr.staff.userId = :staffId) AND " +
           "(:keyword IS NULL OR cr.customerName LIKE %:keyword% OR cr.phone LIKE %:keyword%)")
    Page<ConsultationRequest> findByFilters(
        @Param("status") ConsultationRequest.RequestStatus status,
        @Param("staffId") Long staffId, 
        @Param("keyword") String keyword,
        Pageable pageable
    );
} 