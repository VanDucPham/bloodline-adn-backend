package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "testing_kits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestingKit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kitId;

    private String kitCode;        // Mã kit
    private String kitType;        // Loại kit (BLOOD, SALIVA, HAIR)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment; // Liên kết với lịch hẹn

    private LocalDateTime sentDate;     // Ngày gửi kit
    private LocalDateTime returnDate;   // Ngày nhận lại kit
    private String trackingNumber;      // Số theo dõi (nếu cần)

    @Column(columnDefinition = "TEXT")
    private String notes;               // Ghi chú

    @PrePersist
    protected void onCreate() {
        if (sentDate == null) {
            sentDate = LocalDateTime.now();
        }
    }

}
