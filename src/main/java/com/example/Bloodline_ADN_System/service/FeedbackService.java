package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.noneWhere.FeedbackDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.FeedbackResponse;

import java.util.List;
import java.util.Map;

public interface FeedbackService {
    FeedbackResponse createFeedback(FeedbackDTO feedbackDTO, String userEmail);
    FeedbackResponse getFeedbackByAppointmentId(Long appointmentId);
    List<FeedbackResponse> getFeedbackByUserEmail(String userEmail);
    FeedbackResponse getFeedbackByIdForUser(Long feedbackId, String userEmail);
    List<FeedbackResponse> getAllFeedback();
    void deleteFeedback(Long feedbackId);
    Map<String, Object> getFeedbackStats();
    
    // Public methods - không cần đăng nhập
    List<FeedbackResponse> getFeedbackByServiceId(Long serviceId);
    Map<String, Object> getFeedbackStatsByServiceId(Long serviceId);
}
