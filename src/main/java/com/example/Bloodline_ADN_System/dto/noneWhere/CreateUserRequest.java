package com.example.Bloodline_ADN_System.dto.noneWhere;

import lombok.Data;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data

public class CreateUserRequest {
    private String name ;
    private String email ;
    private String password ;
    private String role ;
}
