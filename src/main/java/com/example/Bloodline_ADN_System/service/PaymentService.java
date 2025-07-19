package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.Entity.Payment;
import com.example.Bloodline_ADN_System.dto.PaymentDTO;
import com.example.Bloodline_ADN_System.payment.PaymentRequest;

public interface PaymentService {
    Payment createPayment(PaymentRequest paymentRequest, Long appointmentId);
    PaymentDTO createPaymentAndReturnDTO(PaymentRequest paymentRequest, Long appointmentId);
}
