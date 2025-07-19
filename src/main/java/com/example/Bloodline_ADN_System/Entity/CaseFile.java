package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "case_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long caseId;

    @Column(name = "case_code", unique = true, nullable = false)
    private String caseCode; // VD: CF-20240619-0001

    @Column(name = "case_type")
    @Enumerated(EnumType.STRING)
    private CaseType caseType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User createdBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @OneToMany(mappedBy = "caseFile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum CaseType {
        ADMINISTRATIVE, CIVIL
    }


}

