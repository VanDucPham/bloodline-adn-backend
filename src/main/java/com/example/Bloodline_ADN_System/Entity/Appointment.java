package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_assigned_id")
    private User assignedStaff; // Nhân viên được phân công

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participant> participants = new ArrayList<>();

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type")
    private AppointmentType type;

    private LocalDateTime createdTime;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    // Trong Appointment.java
    @Column(length = 255)
    private String collectionAddress; // Địa chỉ lấy mẫu tại nhà

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status")
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)

    @Column(name = "delivery_method")
    private DeliveryMethod deliveryMethod;



    @Enumerated(EnumType.STRING)
    private CollectionStatus collectionStatus; // Trạng thái thu mẫu



    @Column(columnDefinition = "TEXT")
    private String appointmentNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "case_id")
    private CaseFile caseFile;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Result result;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Feedback feedback;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private QRCode qrCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "kit_status")
    private KitStatus kitStatus;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        if (deliveryMethod == DeliveryMethod.HOME_COLLECTION) {
            collectionStatus = CollectionStatus.ASSIGNED;
        }
    }
    public enum KitStatus {
        NOT_REQUIRED, // Trường hợp không dùng kit
        KIT_SENT,
        USER_COLLECTED,
        RETURNED
    }

    public enum AppointmentType { ADMINISTRATIVE, CIVIL }
    public enum DeliveryMethod { HOME_COLLECTION, SELF_DROP_OFF , HOME_DELIVERY }
    public enum AppointmentStatus { SCHEDULED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED }
    public enum CollectionStatus {
        ASSIGNED,      // Đã phân công nhân viên
        TRAVELING,     // Nhân viên đang di chuyển
        ARRIVED,       // Đã đến địa chỉ khách hàng
        COLLECTING,    // Đang thu mẫu
        
        COMPLETED      // Hoàn thành thu mẫu
    }
}
