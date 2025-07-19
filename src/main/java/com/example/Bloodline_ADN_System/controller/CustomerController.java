package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.dto.noneWhere.ApiMessResponse;
import com.example.Bloodline_ADN_System.dto.noneWhere.ChangePasswordDTO;
import com.example.Bloodline_ADN_System.dto.*;
import com.example.Bloodline_ADN_System.dto.TrackingAppoint.AppointmentResponseDTO;
import com.example.Bloodline_ADN_System.dto.TrackingAppoint.UpdateParticipant;
import com.example.Bloodline_ADN_System.dto.noneWhere.UserUpdateDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentDTO;
import com.example.Bloodline_ADN_System.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final UserService userService;
    private final UserService userServiceImpl;
    private final AppointmentService appointmentService;
    private final CustomerService customerServiceImp;
    private final SampleService sampleServiceImpl;
    private final CustomerService customerService;
    private final ParticipantService participantService;
    private final ResultService resultService;

    @PutMapping
    public ResponseEntity<UserUpdateDTO> updateUser(Authentication authentication, @RequestBody UserUpdateDTO updatedUser) {
        String email = authentication.getName() ;
        UserUpdateDTO response = userServiceImpl.updateUser(email, updatedUser);
        System.out.println("Đã vào được"+ response.getBirthDate());

        return ResponseEntity.ok(response);
    }
    @PutMapping("/changePassword/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody ChangePasswordDTO dto) {
        userService.changePassword(id, dto);
        return ResponseEntity.ok("Mật khẩu cập nhật thành công");
    }
    @GetMapping ("/Profile")
    public ResponseEntity<UserUpdateDTO> getProfile(Authentication authentication) {
        System.out.println("Vào đdudocwjcj roi");
        String email = authentication.getName();

        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
    @GetMapping("/appointmentList")
    public ResponseEntity<?> getAppointmentList(Authentication authentication) {
        String email = authentication.getName(); // lấy email từ phiên đăng nhập
        Long userId = appointmentService.getUserIdByUsername(email); // lấy userId từ email
        List<AppointmentResponseDTO> appointments = customerServiceImp.getAllAppointments(userId);
        return ResponseEntity.ok(appointments);
    }
    @PutMapping("/{participantId}/update")
    public ResponseEntity<ApiMessResponse> updateParticipant(
            @PathVariable Long participantId,
            @RequestBody UpdateParticipant dto) {
        try {
            boolean updated = participantService.updateParticipant(participantId, dto);
            if (updated) {
                return ResponseEntity.ok(new ApiMessResponse(true, "Cập nhật thành công"));
            }
            return ResponseEntity.badRequest().body(new ApiMessResponse(false, "Không tìm thấy người tham gia"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiMessResponse(false, "Lỗi server khi cập nhật"));
        }
    }

    @GetMapping("/result/get/{appointmentId}")
    public ResultDTO getResult(@PathVariable Long appointmentId){
        return resultService.getResultByAppointmentId(appointmentId);
    }

}
