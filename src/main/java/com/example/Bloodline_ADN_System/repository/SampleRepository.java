package com.example.Bloodline_ADN_System.repository;

import com.example.Bloodline_ADN_System.Entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<Sample, Long> {
    Sample findByParticipant_ParticipantId(Long participantId);
}