package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.dto.noneWhere.ChangePasswordDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.UserUpdateDTO;
import com.example.Bloodline_ADN_System.repository.UserRepository;
import com.example.Bloodline_ADN_System.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserUpdateDTO updateUser(String email, UserUpdateDTO updatedUser) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: " + email));

        if (updatedUser.getName() != null) user.setName(updatedUser.getName().toUpperCase());
        if (updatedUser.getGender() != null) user.setGender(updatedUser.getGender().toUpperCase());
        if (updatedUser.getAddress() != null) user.setAddress(updatedUser.getAddress());
        if (updatedUser.getPhone() != null) user.setPhone(updatedUser.getPhone());
        if (updatedUser.getBirthDate() != null) user.setBirthDate(updatedUser.getBirthDate());
        if (updatedUser.getIndentifiCard() != null) user.setCitizenId(updatedUser.getIndentifiCard());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());

        userRepository.save(user);
        return toDTO(user);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId) ;
    }

    @Override
    public UserUpdateDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    private UserUpdateDTO toDTO(User user) {
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getName());
        dto.setGender(user.getGender());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setEmail(user.getEmail());
        dto.setIndentifiCard(user.getCitizenId());
        dto.setBirthDate(user.getBirthDate());
        dto.setRole(User.UserRole.valueOf(user.getRole().toString()));
      //  dto.setJoined(user.getJoinedDate()); // nếu bạn có trường này trong entity
        return dto;
    }
}
