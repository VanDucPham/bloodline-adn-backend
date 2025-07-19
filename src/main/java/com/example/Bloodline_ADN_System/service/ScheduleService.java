package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.ScheduleManager.StaffDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.request.AppoinmentAsignedStaffDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.response.DayscheduleDTO;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
@Service
public interface ScheduleService {
    // Lấy lịch của cả tháng
    List<DayscheduleDTO> getSchedule(String yearMonth);

    // Lấy lịch theo ngày cụ thể
    DayscheduleDTO getSchedule(LocalDate date);

    // Lấy danh sách tất cả nhân viên (dành cho phân công)
    List<StaffDTO> getAllStaff();

    // Phân công nhân viên xử lý các hồ sơ
    void assignStaffToCase(List<AppoinmentAsignedStaffDTO> assignmentList);

}
