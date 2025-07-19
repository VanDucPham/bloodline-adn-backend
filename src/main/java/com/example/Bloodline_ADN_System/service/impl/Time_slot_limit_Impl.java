package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.TimeSlotLimit;
import com.example.Bloodline_ADN_System.repository.TimeSlotLimitRepository;
import com.example.Bloodline_ADN_System.service.Tiome_slot_limit_Service;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class Time_slot_limit_Impl implements Tiome_slot_limit_Service {

    private final TimeSlotLimitRepository timeSlotLimitRepository;

    public Time_slot_limit_Impl(TimeSlotLimitRepository timeSlotLimitRepository) {
        this.timeSlotLimitRepository = timeSlotLimitRepository;
    }
    @Override
    public List<TimeSlotLimit> getTimeSlotLimit() {
           return timeSlotLimitRepository.findAll(Sort.by("startTime"));

    }
}
