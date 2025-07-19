package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import com.example.Bloodline_ADN_System.Entity.CaseFile;
import com.example.Bloodline_ADN_System.Entity.Participant;
import com.example.Bloodline_ADN_System.Entity.Sample;
import com.example.Bloodline_ADN_System.dto.CaseTrackingDTO;
import com.example.Bloodline_ADN_System.repository.AppointmentRepository;
import com.example.Bloodline_ADN_System.service.CaseTrackingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CaseTrackingServiceImpl implements CaseTrackingService {
    private final AppointmentRepository appointmentRepository;

    public CaseTrackingDTO toCaseTrackingDTO(Appointment appointment) {
        try {
            CaseTrackingDTO dto = new CaseTrackingDTO();

            if (appointment == null || appointment.getCaseFile() == null) return null;

            CaseFile caseFile = appointment.getCaseFile();
            if (caseFile.getCreatedBy() == null || caseFile.getCaseCode() == null) {
                return null;}

            // Các lệnh chuyển đổi DTO như bạn đang có
            dto.setCaseId(appointment.getCaseFile().getCaseCode());
            dto.setCustomer(appointment.getUser().getName());
            dto.setType(appointment.getType().toString());
            dto.setCreatedAt(appointment.getCreatedTime().toLocalDate());
            dto.setStatus(String.valueOf(appointment.getStatus()));
            dto.setStaff(
                    appointment.getAssignedStaff() != null
                            ? appointment.getAssignedStaff().getName()
                            : "Chưa phân công"
            );

            // Timeline
            List<CaseTrackingDTO.TimelineStep> timeline = new ArrayList<>();
            LocalDate createdDate = appointment.getCreatedTime().toLocalDate();
            timeline.add(new CaseTrackingDTO.TimelineStep("Tiếp nhận", createdDate.toString()));

            // Lấy mẫu
            LocalDate sampleDate = appointment.getParticipants().stream()
                    .map(Participant::getSample)
                    .filter(Objects::nonNull)
                    .map(Sample::getCollectionDateTime)
                    .filter(Objects::nonNull)
                    .map(LocalDateTime::toLocalDate)
                    .min(LocalDate::compareTo)
                    .orElse(null);

            if (sampleDate != null) {
                timeline.add(new CaseTrackingDTO.TimelineStep("Lấy mẫu", sampleDate.toString()));
            }


            if (appointment.getResult() != null && appointment.getResult().getResultDate() != null) {
                LocalDate resultDate = appointment.getResult().getResultDate().toLocalDate();
                timeline.add(new CaseTrackingDTO.TimelineStep("Đã trả kết quả", resultDate.toString()));
            }

            dto.setTimeline(timeline);
            return dto;
        } catch (Exception e) {
            System.err.println("Lỗi khi convert appointment: " + appointment.getAppointmentId());
            e.printStackTrace();
            throw e;
        }
    }


    public List<CaseTrackingDTO> getAllCaseTracking() {
        List<Appointment> appointments = appointmentRepository.findAll();

        return appointments.stream()
                .peek(app -> {
                    if (app.getCaseFile() == null) {
                        System.err.println("⚠Appointment ID " + app.getAppointmentId() + " không có caseFile.");
                    }
                })
                .map(app -> {
                    try {
                        return toCaseTrackingDTO(app);
                    } catch (Exception e) {
                        System.err.println("Lỗi khi convert appointment: " + app.getAppointmentId());
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public CaseTrackingDTO getCaseTrackingByCaseCode(String caseCode) {
        Appointment appointment = appointmentRepository.findByCaseFile_CaseCode(caseCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ có mã: " + caseCode));
        return toCaseTrackingDTO(appointment);
    }
}
