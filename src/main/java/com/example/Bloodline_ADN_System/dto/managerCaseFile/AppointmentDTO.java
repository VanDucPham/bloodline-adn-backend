package com.example.Bloodline_ADN_System.dto.managerCaseFile;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {

    private Long appointmentId;

    // Thông tin người dùng
    private Long userId;

    //Nhân viên phụ trách
    private Long AssignedStaff;

    // Thông tin dịch vụ
    private Long serviceId;

    // Kiểu lịch hẹn (Hành chính / Dịch vụ)
    private Appointment.AppointmentType appointmentType;

    // Ngày và giờ
    private LocalDate appointmentDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")

    private LocalTime appointmentTime;

    // Trạng thái (SCHEDULED, CANCELLED, COMPLETED...)
    private Appointment.AppointmentStatus appointmentStatus;

    // Hình thức nhận kết quả (Tại nhà, tại cơ sở...)
    private String deliveryMethod;

    private String collectionAddress ;
    private String collectionCity;     // Thành phố lấy mẫu
    private String collectionDistrict; // Quận/Huyện lấy mẫu

    // Ghi chú
    private String appointmentNote;

    private String caseCode;
    private Appointment.CollectionStatus collectionStatus;
    private String kit_status ;

    // Danh sách người tham gia
    private List<ParticipantDTO> participants;

    // Danh sách mẫu
    private List<SampleDTO> samples;
}


