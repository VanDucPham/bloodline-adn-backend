package com.example.Bloodline_ADN_System.dto.noneWhere;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
