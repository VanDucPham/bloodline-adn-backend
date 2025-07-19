package com.example.Bloodline_ADN_System.repository;

import com.example.Bloodline_ADN_System.Entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ResultRepository  extends JpaRepository<Result, Long> {
    Optional<Result> findByAppointment_AppointmentId(Long appointmentId);

}




