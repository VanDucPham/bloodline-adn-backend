package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.config.JwtService;
import com.example.Bloodline_ADN_System.dto.noneWhere.RegisterRequest;
import com.example.Bloodline_ADN_System.dto.Login.LoginRequest;
import com.example.Bloodline_ADN_System.dto.Login.LoginResponse;
import com.example.Bloodline_ADN_System.repository.UserRepository;
import com.example.Bloodline_ADN_System.service.AuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        if (!encoder.matches(request.getPassword(),user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        String token = jwtService.generateToken(user);
        System.out.println();
        return new LoginResponse(token, "Đăng nhập thành công", user.getUserId(),user.getName(),  user.getRole(), user.getEmail());
    }

    public LoginResponse loginWithGoogle(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList("367800881851-0vas7cftbs3afhorluk2u9ipp4btttm9.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("Token Google không hợp lệ");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // Nếu người dùng chưa tồn tại → tạo mới
            User user = userRepo.findByEmail(email).orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setPassword(""); // Không cần password nếu dùng Google
                newUser.setRole(User.UserRole.valueOf("CUSTOMER"));
                newUser.setStatusFromString("ACTIVE");
                return userRepo.save(newUser);
            });

            String token = jwtService.generateToken(user);
            return new LoginResponse(token, "Đăng nhập bằng Google thành công", user.getUserId(), user.getName(), user.getRole(), user.getEmail());

        } catch (Exception e) {
            throw new RuntimeException("Xác thực Google thất bại: " + e.getMessage());
        }
    }

    @Override
    public String RegisterUser(RegisterRequest registerRequest) {
        if (userRepo.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exist");
        }
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode((registerRequest.getPassword())));
        user.setPhone(registerRequest.getPhone());
        user.setName(registerRequest.getFullName());
        // Convert role string to enum
        user.setRole(User.UserRole.valueOf("CUSTOMER"));
        user.setStatusFromString("ACTIVE");
        userRepo.save(user);
        return "Thành Công";
    }

    // Lấy tên user theo ID (public)
    public String getUserNameById(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));
        return user.getName();
    }
}