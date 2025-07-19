package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import com.example.Bloodline_ADN_System.Entity.Payment;
import com.example.Bloodline_ADN_System.dto.PaymentDTO;
import com.example.Bloodline_ADN_System.payment.PaymentRequest;
import com.example.Bloodline_ADN_System.repository.AppointmentRepository;
import com.example.Bloodline_ADN_System.repository.PaymentRepository;
import com.example.Bloodline_ADN_System.service.AppointmentService;
import com.example.Bloodline_ADN_System.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImp implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    AppointmentRepository appointmentRepository;
    @Override
    @Transactional
    public Payment createPayment(PaymentRequest paymentRequest, Long appointmentId) {
        Payment payment = new Payment();
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn với ID: " + appointmentId));
        payment.setAppointment(appointment);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentRequest.getPaymentMethod()));
        payment.setStatus(Payment.PaymentStatus.valueOf("COMPLETED"));
        payment.setAmount((double) paymentRequest.getAmount());
        paymentRepository.save(payment);
        return payment  ;
    }
    @Transactional
    public PaymentDTO createPaymentAndReturnDTO(PaymentRequest paymentRequest, Long appointmentId) {
        Payment payment = createPayment(paymentRequest, appointmentId);
        return convertToDTO(payment);
    }



    public PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();

        dto.setPaymentId(payment.getPaymentId());

        if (payment.getAppointment() != null) {
            dto.setAppointmentId(payment.getAppointment().getAppointmentId());
            // Nếu có thông tin dịch vụ trong appointment hoặc đối tượng service
            if (payment.getAppointment().getService() != null) {
                dto.setServiceName(payment.getAppointment().getService().getServiceName());
            } else {
                // Hoặc nếu serviceName nằm trực tiếp trong appointment
                dto.setServiceName(payment.getAppointment().getService().getServiceName());
            }

        }

        dto.setAmount(payment.getAmount());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setNotes(payment.getNotes());

        return dto;
    }

}
