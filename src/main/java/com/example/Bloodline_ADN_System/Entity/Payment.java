package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

// ========================
// 💵 PAYMENT ENTITY
// ========================
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    @JsonIgnore
    private Appointment appointment;

    private Double amount;
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @PrePersist
    protected void onCreate() {
        paymentDate = LocalDateTime.now();
    }

    public enum PaymentMethod { CREDIT_CARD, BANK_TRANSFER, CASH, E_WALLET }
    public enum PaymentStatus { PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED }
}
