package com.example.Bloodline_ADN_System.dto.noneWhere;

import com.example.Bloodline_ADN_System.Entity.User;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDTO {
    private Long userId;
    private String name;
    private String gender;
    private String phone;
    private String address;
    private String email;
    private String indentifiCard;
    private LocalDate birthDate;
    private User.UserRole role;
   // private LocalDate joined; // thêm trường ngày đăng ký (nếu cần hiển thị trên FE)
}
