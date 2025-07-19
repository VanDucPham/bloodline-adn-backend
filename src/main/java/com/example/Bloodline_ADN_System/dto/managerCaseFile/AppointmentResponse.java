package com.example.Bloodline_ADN_System.dto.managerCaseFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse<T> {
    private String message;
    private T data;

}
