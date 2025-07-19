package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// ========================
// 📊 RESULT ENTITY
// ========================
@Entity
@Table(name = "results")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

//    @Enumerated(EnumType.STRING)
//    private TestType testType;

    @Column(columnDefinition = "TEXT")
    private String resultValue;

    private LocalDateTime resultDate;

    @Enumerated(EnumType.STRING)
    private ResultStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

//    public enum TestType { DNA_PATERNITY, DNA_MATERNITY, DNA_SIBLINGSHIP, DNA_GRANDPARENTAGE }
    public enum ResultStatus { PENDING, IN_PROGRESS, COMPLETED, REVIEWED }
}
