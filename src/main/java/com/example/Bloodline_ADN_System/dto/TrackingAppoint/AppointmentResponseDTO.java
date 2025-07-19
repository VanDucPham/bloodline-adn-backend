package com.example.Bloodline_ADN_System.dto.TrackingAppoint;

import com.example.Bloodline_ADN_System.dto.PaymentDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.ParticipantResponeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDTO {
    private String appointmentId;
    private Long serviceId; // Thêm trường serviceId để frontend có thể sử dụng cho feedback
    private Long userId; // Thêm trường userId để frontend có thể sử dụng cho feedback
    private String CaseCode ;
    private String ServiceName ;
    private String CaseType ;
    private LocalDate Date ;
    private LocalTime Time ;
    private String StatusAppointment ;
    private  String delivery_method ;
    private String collection_Status ;
    private String kit_status ;
    private String result; // Thêm trường result để lưu kết quả xét nghiệm
    List<ParticipantResponeDTO> participantResponseDTOS ;
    paymentDTO paymentDTO ;
}
