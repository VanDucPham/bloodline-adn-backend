package com.example.Bloodline_ADN_System.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefundRequest {
    private String trantype;
    private String orderId;
    private String amount;
    private String transDate;
    private String user;
}
