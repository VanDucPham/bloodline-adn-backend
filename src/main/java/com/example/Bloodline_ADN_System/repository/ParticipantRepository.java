package com.example.Bloodline_ADN_System.repository;

import com.example.Bloodline_ADN_System.Entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByAppointment_AppointmentId(Long appointmentId);

}