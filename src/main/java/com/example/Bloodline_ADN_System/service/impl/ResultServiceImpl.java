package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import com.example.Bloodline_ADN_System.Entity.Participant;
import com.example.Bloodline_ADN_System.Entity.Result;
import com.example.Bloodline_ADN_System.Entity.Sample;
import com.example.Bloodline_ADN_System.dto.ResultDTO;
import com.example.Bloodline_ADN_System.repository.AppointmentRepository;
import com.example.Bloodline_ADN_System.repository.ParticipantRepository;
import com.example.Bloodline_ADN_System.repository.ResultRepository;
import com.example.Bloodline_ADN_System.repository.SampleRepository;
import com.example.Bloodline_ADN_System.service.ResultService;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ByteArrayResource;


import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    private final AppointmentRepository appointmentRepository;
    private final ParticipantRepository participantRepository;
    private final SampleRepository sampleRepository;

    //lấy kết quả từ appointment
    @Transactional
    public ResultDTO getResultByAppointmentId(Long appointmentId) {
        return resultRepository.findByAppointment_AppointmentId(appointmentId)
                .map(this::toDTO)
                .orElse(null);
    }

    //kiểm tra xem đã đủ điều kiện để nhập kết quả chưa
    public String validateAppointmentForResult(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch hẹn"));

        List<Participant> participants = participantRepository.findByAppointment_AppointmentId(appointmentId);
        if (participants.size() < 2) {
            return "Lịch hẹn cần ít nhất 2 người tham gia.";
        }

        for (Participant participant : participants) {
            Sample sample = sampleRepository.findByParticipant_ParticipantId(participant.getParticipantId());
            if (sample == null) {
                return "Người tham gia " + participant.getName() + " chưa có mẫu.";
            }
            if (sample.getStatus() != Sample.SampleStatus.COMPLETED) {
                return "Mẫu của người tham gia " + participant.getName() + " chưa được phân tích.";
            }
        }
        return null; // OK
    }

    //staff nhập kết quả
    @Transactional
    public ResultDTO createResult(ResultDTO dto) {
        // 1. Kiểm tra dữ liệu hợp lệ bằng hàm đã có
        String validationMessage = validateAppointmentForResult(dto.getAppointmentId());
        if (validationMessage != null) {
            throw new IllegalArgumentException(validationMessage);
        }

        // 2. Lấy appointment
        Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lịch hẹn"));

        Result result = new Result();
        result.setAppointment(appointment);
        result.setResultValue(dto.getResultValue());
        result.setNotes(dto.getNotes());
        result.setStatus(Result.ResultStatus.COMPLETED);
        result.setResultDate(LocalDateTime.now());  // ngày giờ hiện tại

        // 4. Lưu vào DB và trả về DTO
        Result saved = resultRepository.save(result);
        return toDTO(saved);
    }

    //xuất file pdf cho kết quả
    @Transactional
    public ResponseEntity<ByteArrayResource> exportResultPdf(Long appointmentId) {

        ResultDTO resultDTO = getResultByAppointmentId(appointmentId);
        if (resultDTO == null) {
            throw new IllegalArgumentException("Không tìm thấy kết quả cho lịch hẹn ID: " + appointmentId);
        }

        try {
           ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Nhúng font Unicode hỗ trợ tiếng Việt (đường dẫn thật của font trong resources/fonts)
            String fontPath = getClass().getResource("/fonts/Roboto-Regular.ttf").toExternalForm();
            PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);
            document.setFont(font);


            // Format ngày rõ ràng
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDate = resultDTO.getResultDate().format(formatter);

            document.add(new Paragraph("KẾT QUẢ PHÂN TÍCH ADN").setBold().setFontSize(16));
            document.add(new Paragraph("Mã lịch hẹn: " + resultDTO.getAppointmentId()));
            document.add(new Paragraph("Loại lịch hẹn: " + resultDTO.getAppointmentType()));
            if (resultDTO.getParticipants() != null && !resultDTO.getParticipants().isEmpty()) {
                String participantsStr = String.join(", ", resultDTO.getParticipants());
                document.add(new Paragraph("Người tham gia: " + participantsStr));
            }
            document.add(new Paragraph("Kết quả: " + resultDTO.getResultValue()));
            document.add(new Paragraph("Ghi chú: " + resultDTO.getNotes()));
            document.add(new Paragraph("Ngày phân tích: " + formattedDate));
            document.add(new Paragraph("Trạng thái: " + resultDTO.getStatus()));
            document.close();

            ByteArrayResource resource = new ByteArrayResource(out.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=result_" + appointmentId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo file PDF: " + e.getMessage(), e);
        }
    }


    private ResultDTO toDTO(Result result) {
        ResultDTO dto = new ResultDTO();

        dto.setResultId(result.getResultId());
        dto.setAppointmentId(result.getAppointment().getAppointmentId());
        dto.setResultValue(result.getResultValue());
        dto.setResultDate(result.getResultDate());
        dto.setNotes(result.getNotes());
        dto.setStatus(result.getStatus());

        Appointment appointment = result.getAppointment();
        if (appointment != null) {
            dto.setAppointmentType(appointment.getType().toString());
            if (appointment.getParticipants() != null) {
            List<String> participantNames = appointment.getParticipants().stream()
                    .map(Participant::getName)
                    .collect(Collectors.toList());
            dto.setParticipants(participantNames);
        }
        }
        return dto;
    }
}
