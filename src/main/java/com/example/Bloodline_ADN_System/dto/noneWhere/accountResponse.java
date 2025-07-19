package com.example.Bloodline_ADN_System.dto.noneWhere;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class accountResponse {
    private Long user_id;
    private String name ;
    private String email ;
    private String role ;
  //  private String createAt ;
    private String status ;

}
