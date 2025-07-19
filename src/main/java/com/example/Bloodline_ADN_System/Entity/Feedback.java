package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// ========================
// 💬 FEEDBACK ENTITY
// ========================
@Entity
@Table(name = "feedbacks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long feedbackId;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "service_id")
        private Service service;

        @Column(columnDefinition = "TEXT")
        private String feedbackText;

        private LocalDateTime feedbackDate;
        private Integer rating;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "appointment_id")
        private Appointment appointment;

        @PrePersist
        protected void onCreate() {
                feedbackDate = LocalDateTime.now();
        }
}
