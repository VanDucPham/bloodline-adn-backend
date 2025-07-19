package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

// ========================
// 👤 PARTICIPANT ENTITY
// ========================
@Entity
@Table(name = "participants")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long participantId;

    private String name;
    private String relationship;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String citizenId;
    private String address;
    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    @JsonIgnore
    private Appointment appointment;

    @OneToOne(mappedBy = "participant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Sample sample;

    public enum Gender { MALE, FEMALE, OTHER }
}
