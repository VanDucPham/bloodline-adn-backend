package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import com.example.Bloodline_ADN_System.Entity.CaseFile;
import com.example.Bloodline_ADN_System.Entity.TimeSlotLimit;
import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.StaffDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.request.AppoinmentAsignedStaffDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.response.CaseassignmentDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.response.DayscheduleDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.response.ShiftDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.ServiceDTO;
import com.example.Bloodline_ADN_System.repository.AppointmentRepository;
import com.example.Bloodline_ADN_System.repository.TimeSlotLimitRepository;
import com.example.Bloodline_ADN_System.repository.UserRepository;
import com.example.Bloodline_ADN_System.service.ScheduleService;
import org.hibernate.sql.ast.tree.update.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TimeSlotLimitRepository timeSlotLimitRepository;
    @Override
    public List<DayscheduleDTO> getSchedule(String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth); // e.g., "2025-07"

        // Lấy danh sách lịch hẹn trong tháng
        List<Appointment> allAppointments = appointmentRepository.findByMonth(yearMonth);
        List<TimeSlotLimit> slotLimits = timeSlotLimitRepository.findAll();

        // Tìm tất cả ngày có lịch hẹn
        List<LocalDate> uniqueDates = allAppointments.stream()
                .map(Appointment::getAppointmentDate)
                .distinct()
                .collect(Collectors.toList());

        List<DayscheduleDTO> dayscheduleDTOList = new ArrayList<>();

        for (LocalDate date : uniqueDates) {
            List<Appointment> appointmentsForDate = allAppointments.stream()
                    .filter(a -> a.getAppointmentDate().equals(date))
                    .collect(Collectors.toList());

            List<ShiftDTO> shifts = new ArrayList<>();

            for (Appointment a : appointmentsForDate) {
                List<CaseassignmentDTO> caseList = new ArrayList<>();

                if (a.getCaseFile() != null) {
                    CaseassignmentDTO caseDTO = new CaseassignmentDTO();
                    caseDTO.setAppointmentID(a.getAppointmentId());
                    caseDTO.setCaseCode(a.getCaseFile().getCaseCode());
                    caseDTO.setAppointmentType(String.valueOf(a.getType()));
                    caseDTO.setDeliveryMethod(String.valueOf(a.getDeliveryMethod()));
                    caseDTO.setAssignStaff(a.getAssignedStaff() != null ? a.getAssignedStaff().getName() : "");
                    caseDTO.setCustomerName(a.getUser() != null ? a.getUser().getName() : "");
                    caseList.add(caseDTO);
                }

                // 👉 Tìm time range phù hợp từ TimeSlotLimit
                String timeRange = a.getAppointmentTime() != null
                        ? slotLimits.stream()
                        .filter(s -> !s.getStartTime().isAfter(a.getAppointmentTime())
                                && a.getAppointmentTime().isBefore(s.getEndTime()))
                        .map(s -> s.getStartTime().toString() + " - " + s.getEndTime().toString())
                        .findFirst()
                        .orElse(a.getAppointmentTime().toString())
                        : "";

                ShiftDTO shiftDTO = new ShiftDTO(
                        timeRange,
                        a.getAssignedStaff() != null ? a.getAssignedStaff().getName() : "",
                        caseList
                );

                shifts.add(shiftDTO);
            }

            dayscheduleDTOList.add(new DayscheduleDTO(date, shifts));
        }

        return dayscheduleDTOList;
    }




    @Override
    public DayscheduleDTO getSchedule(LocalDate date) {
        return null;
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        List<User> staff =  userRepository.getAllIsStaff();

        return staff.stream().map(u -> new StaffDTO(u.getUserId(), u.getName(), u.getStaffCode())).collect(Collectors.toList());

    }

    @Override
    public void assignStaffToCase(List<AppoinmentAsignedStaffDTO> assignmentList) {
        for (AppoinmentAsignedStaffDTO assignment : assignmentList) {
            // Tìm nhân viên theo staffCode
            User user = userRepository.findByStaffCode(assignment.getStaffCode());

            if (user != null) {
                // Tìm lịch hẹn theo ID
                Optional<Appointment> assign = appointmentRepository.findById(assignment.getAppointmentId());

                assign.ifPresent(appointment -> {
                    appointment.setAssignedStaff(user);
                    appointmentRepository.save(appointment); // cập nhật vào DB
                });
            }
        }
    }

}
