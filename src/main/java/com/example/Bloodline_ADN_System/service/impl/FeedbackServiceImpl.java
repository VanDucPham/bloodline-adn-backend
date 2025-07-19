package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import com.example.Bloodline_ADN_System.Entity.Feedback;
import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.dto.noneWhere.FeedbackDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.FeedbackResponse;
import com.example.Bloodline_ADN_System.repository.AppointmentRepository;
import com.example.Bloodline_ADN_System.repository.FeedbackRepository;
import com.example.Bloodline_ADN_System.repository.ServiceRepository;
import com.example.Bloodline_ADN_System.repository.UserRepository;
import com.example.Bloodline_ADN_System.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final ServiceRepository serviceRepository;

    // Customer tạo feedback với kiểm tra quyền
    @Override
    public FeedbackResponse createFeedback(FeedbackDTO feedbackDTO, String userEmail) {
        // Kiểm tra user có tồn tại và có role CUSTOMER không
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        
        if (!"CUSTOMER".equals(user.getRole())) {
            throw new RuntimeException("Chỉ khách hàng mới được gửi feedback");
        }

        // Kiểm tra appointment có thuộc về user này không
        Appointment appointment = appointmentRepository.findById(feedbackDTO.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Lịch hẹn không tồn tại"));
        
        if (!appointment.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn chỉ được gửi feedback cho lịch hẹn của mình");
        }

        // Kiểm tra đã gửi feedback cho appointment này chưa
        if (feedbackRepository.findByAppointment_AppointmentId(feedbackDTO.getAppointmentId()).isPresent()) {
            throw new RuntimeException("Bạn đã gửi feedback cho lịch hẹn này rồi");
        }

        com.example.Bloodline_ADN_System.Entity.Service service = serviceRepository.findById(feedbackDTO.getServiceId())
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại"));

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setService(service);
        feedback.setAppointment(appointment);
        feedback.setFeedbackText(feedbackDTO.getFeedbackText());
        feedback.setRating(feedbackDTO.getRating());

        Feedback saved = feedbackRepository.save(feedback);
        return toDTO(saved);
    }

    // Admin/Manager/Staff xem feedback theo appointment
    @Override
    public FeedbackResponse getFeedbackByAppointmentId(Long appointmentId) {
        Feedback feedback = feedbackRepository.findByAppointment_AppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("Feedback không tồn tại " + appointmentId));
        return toDTO(feedback);
    }

    // Customer xem feedback của chính mình
    @Override
    public List<FeedbackResponse> getFeedbackByUserEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        
        List<Feedback> feedbacks = feedbackRepository.findByUser_UserId(user.getUserId());
        return feedbacks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Customer xem chi tiết feedback của mình
    @Override
    public FeedbackResponse getFeedbackByIdForUser(Long feedbackId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback không tồn tại"));
        
        if (!feedback.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Bạn chỉ được xem feedback của mình");
        }
        
        return toDTO(feedback);
    }

    // Admin/Manager/Staff xem tất cả feedback
    @Override
    public List<FeedbackResponse> getAllFeedback() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        return feedbacks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Public: Xem feedback theo service (không cần đăng nhập)
    @Override
    public List<FeedbackResponse> getFeedbackByServiceId(Long serviceId) {
        // Kiểm tra service có tồn tại không
        if (!serviceRepository.existsById(serviceId)) {
            throw new RuntimeException("Dịch vụ không tồn tại");
        }
        
        List<Feedback> feedbacks = feedbackRepository.findByService_ServiceId(serviceId);
        return feedbacks.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Public: Thống kê feedback theo service (không cần đăng nhập)
    @Override
    public Map<String, Object> getFeedbackStatsByServiceId(Long serviceId) {
        // Kiểm tra service có tồn tại không
        if (!serviceRepository.existsById(serviceId)) {
            throw new RuntimeException("Dịch vụ không tồn tại");
        }
        
        List<Feedback> serviceFeedbacks = feedbackRepository.findByService_ServiceId(serviceId);
        
        long totalFeedbacks = serviceFeedbacks.size();
        double avgRating = serviceFeedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
        
        Map<Integer, Long> ratingDistribution = serviceFeedbacks.stream()
                .collect(Collectors.groupingBy(Feedback::getRating, Collectors.counting()));
        
        return Map.of(
            "totalFeedbacks", totalFeedbacks,
            "averageRating", avgRating,
            "ratingDistribution", ratingDistribution
        );
    }

    // Admin xóa feedback vi phạm
    @Override
    public void deleteFeedback(Long feedbackId) {
        if (!feedbackRepository.existsById(feedbackId)) {
            throw new RuntimeException("Feedback không tồn tại");
        }
        feedbackRepository.deleteById(feedbackId);
    }

    // Admin/Manager thống kê feedback
    @Override
    public Map<String, Object> getFeedbackStats() {
        List<Feedback> allFeedbacks = feedbackRepository.findAll();
        
        long totalFeedbacks = allFeedbacks.size();
        double avgRating = allFeedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
        
        Map<Integer, Long> ratingDistribution = allFeedbacks.stream()
                .collect(Collectors.groupingBy(Feedback::getRating, Collectors.counting()));
        
        return Map.of(
            "totalFeedbacks", totalFeedbacks,
            "averageRating", avgRating,
            "ratingDistribution", ratingDistribution
        );
    }

    private FeedbackResponse toDTO(Feedback feedback) {
        FeedbackResponse response = new FeedbackResponse();
        response.setFeedbackId(feedback.getFeedbackId());
        response.setUserId(feedback.getUser().getUserId());
        response.setServiceId(feedback.getService().getServiceId());
        response.setAppointmentId(feedback.getAppointment().getAppointmentId());
        response.setFeedbackText(feedback.getFeedbackText());
        response.setRating(feedback.getRating());
        response.setFeedbackDate(feedback.getFeedbackDate());
        return response;
    }
}
