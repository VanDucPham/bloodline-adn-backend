package com.example.Bloodline_ADN_System.Entity;

import com.example.Bloodline_ADN_System.dto.noneWhere.SampleDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// ========================
// 🧪 SAMPLE ENTITY
// ========================
@Entity
@Table(name = "samples")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sampleId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    @JsonIgnore
    private Participant participant;


    private String sampleType;

    @Column(name = "collection_datetime")
    private LocalDateTime collectionDateTime;

    @Enumerated(EnumType.STRING)
    private SampleQuality quality;

    @Enumerated(EnumType.STRING)
    private SampleStatus status;

    private String result;

    @Column(columnDefinition = "TEXT")
    private String notes;


    public enum SampleQuality { EXCELLENT, GOOD, FAIR, POOR }
    public enum SampleStatus {
        COLLECTED, PROCESSING, ANALYZED, COMPLETED
    }

}