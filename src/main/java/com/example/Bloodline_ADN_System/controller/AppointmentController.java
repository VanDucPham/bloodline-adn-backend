package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import com.example.Bloodline_ADN_System.dto.ManagerService.ServiceManagerDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.ApiMessResponse;
import com.example.Bloodline_ADN_System.dto.noneWhere.AppointmentCheckRequest;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentRequest;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentResponse;
import com.example.Bloodline_ADN_System.service.AppointmentService;
import com.example.Bloodline_ADN_System.service.ServiceList;
import com.example.Bloodline_ADN_System.service.impl.ServiceImpl;
import com.example.Bloodline_ADN_System.service.impl.Time_slot_limit_Impl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customer/appointment")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final Time_slot_limit_Impl timeSlotLimitService;
    private final ServiceImpl serviceImpl;
    private final ServiceList serviceList;

    public AppointmentController(AppointmentService appointmentService,
                                 Time_slot_limit_Impl timeSlotLimitService,
                                 ServiceImpl serviceImpl, ServiceList serviceList) {
        this.appointmentService = appointmentService;
        this.timeSlotLimitService = timeSlotLimitService;
        this.serviceImpl = serviceImpl;
        this.serviceList = serviceList;
    }

    /**
     * Kiểm tra lịch trống dựa trên ngày giờ và email người dùng
     */
    @PostMapping("/check-availability")
    public ResponseEntity<String> checkAvailability1(@RequestBody AppointmentCheckRequest request,
                                                    Authentication authentication) {
        String email = authentication.getName();
        boolean available = appointmentService.checkAvailability(
                request.getAppointmentDate(),
                request.getAppointmentTime(),
                email
        );
        return ResponseEntity.ok(available ? "Lịch trống." : "Lịch không trống.");
    }

    /**
     * Tạo mới một lịch hẹn
     */
    @PostMapping("/create")
    public ResponseEntity<AppointmentResponse<AppointmentDTO>> createAppointment(
            @RequestBody AppointmentRequest request) {
        AppointmentResponse<AppointmentDTO> response = appointmentService.createAppointment(request);
        return ResponseEntity.status(201).body(response);
    }

    /**
     * Lấy tất cả lịch hẹn (admin hoặc nhân viên)
     */
    @GetMapping("/all")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointment());
    }

    /**
     * Lấy lịch hẹn theo ID người dùng
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(appointmentService.getAppointmentByUserId(userId));
    }

    /**
     * Hủy lịch hẹn
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiMessResponse> cancelAppointment(@PathVariable Long id) {
        ApiMessResponse result = appointmentService.cancelAppointment(id);

        // Nếu thành công, trả HTTP 200
        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        }

        // ❗ Nếu thất bại logic, vẫn trả HTTP 200 (để FE không bị catch)
        // return ResponseEntity.badRequest().body(result); ❌ → gây lỗi FE
        return ResponseEntity.ok(result); // ✅
    }




    /**
     * Lọc lịch hẹn theo trạng thái, loại và ngày
     */
    @GetMapping("/filter")
    public ResponseEntity<List<AppointmentDTO>> filterAppointments(
            @RequestParam(required = false) Appointment.AppointmentStatus status,
            @RequestParam(required = false) Appointment.AppointmentType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.filterAppointment(status, type, date));
    }

    /**
     * Cập nhật trạng thái lịch hẹn
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentDTO> updateAppointment(@PathVariable Long id, @RequestParam("status") Appointment.AppointmentStatus status,
                                                            @RequestParam("collectionStatus") Appointment.CollectionStatus collectionStatus) {
        return ResponseEntity.ok(appointmentService.updateAppointmentProgress(id, status, collectionStatus));
    }

    /**
     * Lấy danh sách khung giờ hẹn
     */
    @GetMapping("/time-slots")
    public ResponseEntity<?> getAppointmentTimeSlots() {
        return ResponseEntity.ok(timeSlotLimitService.getTimeSlotLimit());
    }

    /**
     * Lấy danh sách dịch vụ
     */
    @GetMapping("/services")
    public ResponseEntity<List<ServiceManagerDTO>> getAppointmentServices() {
        return ResponseEntity.ok(serviceList.getAllServices());
    }
}
