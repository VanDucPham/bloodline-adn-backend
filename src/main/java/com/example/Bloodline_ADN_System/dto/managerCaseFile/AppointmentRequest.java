package com.example.Bloodline_ADN_System.dto.managerCaseFile;

import com.example.Bloodline_ADN_System.Entity.Payment;
import com.example.Bloodline_ADN_System.dto.PaymentDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleDTO;
import com.example.Bloodline_ADN_System.payment.PaymentRequest;
import lombok.Data;

import java.util.List;
@Data
public class AppointmentRequest {
    private AppointmentDTO appointment;
    private List<ParticipantDTO> participants;
    private List<SampleDTO> samples;
    private caseFileDTO caseFile;
     private PaymentRequest payment;
}
