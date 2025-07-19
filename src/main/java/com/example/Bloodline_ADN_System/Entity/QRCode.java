package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// ========================
// ✅ QR CODE ENTITY
// ========================
@Entity
@Table(name = "qrcodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qrId;

    private String code;

    @OneToOne
    @JoinColumn(name= "appointment_id")
    private Appointment appointment;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}