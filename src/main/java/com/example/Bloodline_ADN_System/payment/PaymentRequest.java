package com.example.Bloodline_ADN_System.payment;

import lombok.Data;

@Data
public class PaymentRequest {
    private long amount;
    private String orderInfo;
    private String txnRef;
    private String bankCode; // optional
    private  String language ;
  private String paymentMethod; // optional
}
