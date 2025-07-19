package com.example.Bloodline_ADN_System.controller;


import com.example.Bloodline_ADN_System.dto.noneWhere.BlogDTO;
import com.example.Bloodline_ADN_System.dto.Login.LoginRequest;
import com.example.Bloodline_ADN_System.dto.Login.LoginResponse;
import com.example.Bloodline_ADN_System.dto.noneWhere.RegisterRequest;
import com.example.Bloodline_ADN_System.dto.noneWhere.ServiceDTO;
import com.example.Bloodline_ADN_System.service.BlogService;
import com.example.Bloodline_ADN_System.service.ServiceService;
import com.example.Bloodline_ADN_System.service.impl.AuthServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;
    private final BlogService blogService;
    private final ServiceService serviceService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/google-login")
    public ResponseEntity<LoginResponse> loginWithGoogle(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");
        LoginResponse response = authService.loginWithGoogle(idToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.RegisterUser(registerRequest));
    }

    @PostMapping("/logout")
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @GetMapping("/blog")
    public List<BlogDTO> getAllBlogs() {
        return blogService.getAllBlogDTO();
    }


    @GetMapping("/getService")
    public ResponseEntity<List<ServiceDTO>> getPublicServices() {
        List<ServiceDTO> services = serviceService.getAllServices();
        System.out.println(services.size()
        );
        return ResponseEntity.ok(services);
    }
    @GetMapping("/detailService/{id}")
    public ResponseEntity<ServiceDTO> getPublicServiceById(@PathVariable Long id) {
        ServiceDTO service = serviceService.getServiceById(id);
        return ResponseEntity.ok(service);
    }



    // Public: Lấy thông tin user theo ID (chỉ hiển thị tên)
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            String userName = authService.getUserNameById(userId);
            return ResponseEntity.ok(Map.of("name", userName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

}
