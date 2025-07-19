package com.example.Bloodline_ADN_System.dto.TrackingAppoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateParticipant {
    private String participantId ;
    private String name ;
    private String citizenId ;
    private String relationShip ;
    private UpdateSample sample ;

}
