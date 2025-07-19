package com.example.Bloodline_ADN_System.repository;

import com.example.Bloodline_ADN_System.Entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Feedback> findByAppointment_AppointmentId(Long appointmentId);
    List<Feedback> findByUser_UserId(Long userId);
    List<Feedback> findByService_ServiceId(Long serviceId);
    
    // Đếm số lượng feedback theo service ID
    long countByService_ServiceId(Long serviceId);
}
