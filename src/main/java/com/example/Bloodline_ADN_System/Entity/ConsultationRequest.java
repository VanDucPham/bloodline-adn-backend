package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "consultation_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    private String customerName;        // Tên người cần tư vấn
    private String phone;               // Số điện thoại liên hệ
    private String email;               // Email (nếu có)

    @Column(columnDefinition = "TEXT")
    private String content;             // Nội dung/yêu cầu tư vấn

    private LocalDateTime createdAt;    // Thời gian gửi yêu cầu

    @Enumerated(EnumType.STRING)
    private RequestStatus status;       // Trạng thái xử lý

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handled_by")
    private User staff;                 // Nhân viên đã tiếp nhận (nếu có)

    public enum RequestStatus {
        NEW, IN_PROGRESS, RESOLVED, CLOSED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status = RequestStatus.NEW;
    }
}
