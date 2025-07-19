package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.*;

import com.example.Bloodline_ADN_System.dto.TrackingAppoint.paymentDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.ParticipantResponeDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleDTO;
import com.example.Bloodline_ADN_System.dto.TrackingAppoint.AppointmentResponseDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentDTO;

import com.example.Bloodline_ADN_System.dto.managerCaseFile.AppointmentResponse;
import com.example.Bloodline_ADN_System.repository.AppointmentRepository;
import com.example.Bloodline_ADN_System.repository.PaymentRepository;
import com.example.Bloodline_ADN_System.repository.ResultRepository;
import com.example.Bloodline_ADN_System.repository.ServiceRepository;

import com.example.Bloodline_ADN_System.service.CustomerService;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class CustomerServiceImp implements CustomerService {

    private final AppointmentServiceImpl appointmentService;
    private final ServiceImpl serviceimp;
    private final CaseFileServiceImpl caseFileService;
    private final ParticipantServiceImpl participantService;
    private final PaymentRepository paymentRepository;
    private final SampleServiceImpl sampleService;
    private final ResultRepository resultRepository;
    
    public CustomerServiceImp(AppointmentServiceImpl appointmentService, ServiceImpl serviceimp, CaseFileServiceImpl caseFileService, ParticipantServiceImpl participantService, ServiceImpl serviceService, PaymentRepository paymentRepository, SampleServiceImpl sampleService, ResultRepository resultRepository) {
        this.appointmentService = appointmentService;
        this.serviceimp = serviceimp;

        this.caseFileService = caseFileService;
        this.participantService = participantService;
        this.paymentRepository = paymentRepository;

        this.sampleService = sampleService;
        this.resultRepository = resultRepository;
    }

    @Override
    @Transactional
    public List<AppointmentResponseDTO> getAllAppointments(Long customerId) {
        return appointmentService.getAppointmentByUserId(customerId)
                .stream()
                .map(appointment -> {
                    AppointmentResponseDTO dto = new AppointmentResponseDTO();
                    dto.setAppointmentId(String.valueOf(appointment.getAppointmentId()));
                    dto.setServiceId(appointment.getServiceId());
                    dto.setUserId(appointment.getUserId());
                    dto.setDate(appointment.getAppointmentDate());
                    dto.setTime(appointment.getAppointmentTime());
                    dto.setDelivery_method(appointment.getDeliveryMethod());
                    dto.setCollection_Status(String.valueOf(appointment.getCollectionStatus()));
                    dto.setKit_status(appointment.getKit_status());

                    CaseFile caseFile = caseFileService.findById(
                            appointmentService.findCaseIdByAppointmentId(appointment.getAppointmentId())
                    );
                    String caseCode = caseFile != null ? caseFile.getCaseCode() : "";
                    String caseType = caseFile != null ? String.valueOf(caseFile.getCaseType()) : "";
                    dto.setCaseCode(caseCode);
                    dto.setCaseType(caseType);

                    Optional<Service> optionalService = serviceimp.findServiceById(appointment.getServiceId());
                    dto.setServiceName(optionalService.map(Service::getServiceName).orElse("Không rõ"));
                    dto.setStatusAppointment(String.valueOf(appointment.getAppointmentStatus()));

                    Payment pm = paymentRepository.findPaymentByAppointmentId(appointment.getAppointmentId());
                    paymentDTO payment = new paymentDTO();
                    if (pm != null) {
                        payment.setPaymentID(pm.getPaymentId());
                        payment.setPaymentAmount(pm.getAmount());
                        payment.setPaymentMethod(pm.getPaymentMethod() != null ? pm.getPaymentMethod().name() : null);
                        payment.setPaymentStatus(pm.getStatus() != null ? pm.getStatus().name() : null);
                        payment.setPaymentDate(pm.getPaymentDate());
                    }
                    dto.setPaymentDTO(payment);

                    Optional<Result> resultOptional = resultRepository.findByAppointment_AppointmentId(appointment.getAppointmentId());
                    dto.setResult(resultOptional.map(Result::getResultValue).orElse(null));

                    List<ParticipantResponeDTO> participantList = participantService.getParticipantByAppointmentId(appointment.getAppointmentId());
                    for (ParticipantResponeDTO participant : participantList) {
                        SampleDTO sample = sampleService.getSampleByParticipantId(participant.getParticipantId());
                        participant.setSampleDTO(sample);
                    }
                    dto.setParticipantResponseDTOS(participantList);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateAppointment(AppointmentDTO appointmentDTO, Long appointmentId) {
        appointmentService.updateAppointment(appointmentDTO, appointmentId);
    }
}