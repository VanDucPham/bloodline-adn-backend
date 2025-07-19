package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.dto.noneWhere.CreateUserRequest;
import com.example.Bloodline_ADN_System.dto.noneWhere.accountResponse;
import com.example.Bloodline_ADN_System.dto.noneWhere.updateUserRequest;
import com.example.Bloodline_ADN_System.repository.UserRepository;
import com.example.Bloodline_ADN_System.service.AdminService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void importUserFromExcel(MultipartFile file) throws IOException {
        Workbook workbook =  new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        for(Row row : sheet){
            if(row.getRowNum() == 0){ continue; }
            String name  = row.getCell(0)==null?"": row.getCell(0).getStringCellValue();
            String email = row.getCell(1)==null?"": row.getCell(1).getStringCellValue();
            String password = row.getCell(2)==null?"": row.getCell(2).getStringCellValue();
            String role = row.getCell(3)==null?"": row.getCell(3).getStringCellValue();

            CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setName(name);
            createUserRequest.setEmail(email);
            createUserRequest.setPassword(password);
            createUserRequest.setRole(role);

            createUser(createUserRequest);

        }
    workbook.close();
    }
    @Override
    public void createUser(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }
        System.out.println(request.getName());
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setName(request.getName());

        user.setRole(User.UserRole.valueOf(request.getRole()));
        user.setStatusFromString("ACTIVE");

        userRepository.save(user);
        System.out.println("thêm thành cônh");
        System.out.println(user.getName());

    }

    @Override
    public List<accountResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> new accountResponse(u.getUserId(),u.getName(), u.getEmail(), u.getRole(), u.getStatusString()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public void updateUser(Long id, updateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(User.UserRole.valueOf(request.getRole()));
        user.setStatusFromString(request.getStatus());
        userRepository.save(user);
    }







    @Override
    public void deleteUser(Long id) {
         userRepository.deleteById(id);
    }
}