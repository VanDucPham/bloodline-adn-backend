package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.Participant;
import com.example.Bloodline_ADN_System.Entity.Sample;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.SampleCustomerDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleStaffDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleUpdateDTO;
import com.example.Bloodline_ADN_System.repository.ParticipantRepository;
import com.example.Bloodline_ADN_System.repository.SampleRepository;
import com.example.Bloodline_ADN_System.repository.ServiceRepository;
import com.example.Bloodline_ADN_System.service.SampleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SampleServiceImpl implements SampleService {
    private final SampleRepository sampleRepository;
    private final ParticipantRepository participantRepository;
    private final ServiceRepository serviceRepository;


    public SampleServiceImpl(SampleRepository samppleRepository, ParticipantRepository participantRepository, ServiceRepository serviceRepository) {
        this.sampleRepository = samppleRepository;
        this.participantRepository = participantRepository;
        this.serviceRepository = serviceRepository;
    }

    //Khách hàng điền sample với dịch vụ tự lấy mẫu tại nhà
    @Override
    public List<SampleDTO> createSamplesByCustomer(List<SampleCustomerDTO> dtoList) {
        return dtoList.stream().map(dto -> {
            Participant participant = participantRepository.findById(dto.getParticipantId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người tham gia"));
            Sample sample = new Sample();
            sample.setParticipant(participant);
            sample.setSampleType(dto.getSampleType());
            sample.setCollectionDateTime(dto.getCollectionDateTime());
            sample.setStatus(Sample.SampleStatus.COLLECTED);
            sample.setQuality(null);
            sample.setResult(null);
            sample.setNotes(null);

            return toDTO(sampleRepository.save(sample));
        }).collect(Collectors.toList());
    }

    //Nhân viên điền sample đối với dịch vụ lấy mẫu tại cơ sở
    public List<SampleDTO> createSampleByStaff(List<SampleStaffDTO> dtoList){
        return dtoList.stream().map(dto ->{
            Participant participant = participantRepository.findById(dto.getParticipantId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người tham gia"));
            Sample sample = new Sample();
            sample.setParticipant(participant);
            sample.setSampleType(dto.getSampleType());
            sample.setCollectionDateTime(LocalDateTime.now());
            sample.setStatus(Sample.SampleStatus.COLLECTED);
            sample.setResult(dto.getResult());
            sample.setNotes(dto.getNotes());

            return toDTO(sampleRepository.save(sample));

        }).collect(Collectors.toList());
    }

    //Nhân viên điền thêm thông tin cho sample sau khi nhận được mẫu dành cho dịch vụ tự lấy mẫu
    @Transactional
    public SampleDTO updateSampleInfo(SampleUpdateDTO dto) throws JsonProcessingException {
        Sample sample = sampleRepository.findById(dto.getSampleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mẫu"));
        if (dto.getQuality() != null) {
            sample.setQuality(dto.getQuality());
            sample.setStatus(Sample.SampleStatus.ANALYZED);

        }

        // Cập nhật result nếu có
        if (dto.getResult() != null && !dto.getResult().trim().isEmpty()) {
            sample.setResult(dto.getResult());
            // Khi có kết quả, trạng thái chuyển thành COMPLETED
            sample.setStatus(Sample.SampleStatus.COMPLETED);
        }
        sample.setNotes(dto.getNotes());
        return toDTO(sampleRepository.save(sample));
    }



    public SampleDTO getSampleByParticipantId(Long participantId) {
        Sample sample = sampleRepository.findByParticipant_ParticipantId(participantId);

        if (sample == null) {
            return null;
        }

        SampleDTO dto = new SampleDTO();
        dto.setSampleId(sample.getSampleId());
        dto.setSampleType(sample.getSampleType());
        dto.setCollectionDateTime(sample.getCollectionDateTime());
        dto.setQuality(sample.getQuality());
        dto.setStatus(sample.getStatus());
        dto.setResult(sample.getResult());
        dto.setNotes(sample.getNotes());

        // ⚠️ Tránh sample.getParticipant().getX()
        dto.setParticipantId(participantId); // truyền trực tiếp

        return dto;
    }

    @Override
    @Transactional
    public SampleDTO updateSample(SampleDTO dto) {
        Optional<Sample> optionalSample = sampleRepository.findById(dto.getSampleId());

        if (optionalSample.isPresent()) {
            Sample sample = optionalSample.get();

            if (dto.getSampleType() != null) {
                sample.setSampleType(dto.getSampleType());
            }
            if (dto.getCollectionDateTime() != null) {
                sample.setCollectionDateTime(dto.getCollectionDateTime());
            }
            if (dto.getQuality() != null) {
                sample.setQuality(dto.getQuality());
            }
            if (dto.getStatus() != null) {
                sample.setStatus(dto.getStatus());
            }
            if (dto.getResult() != null) {
                sample.setResult(dto.getResult());
            }
            if (dto.getNotes() != null) {
                sample.setNotes(dto.getNotes());
            }

            Sample updated = sampleRepository.save(sample);
            return toDTO(updated);
        } else {
            throw new RuntimeException("Không tìm thấy sample với ID: " + dto.getSampleId());
        }
    }

    @Override
    public void saveAll(List<Sample> samples) {
        sampleRepository.saveAll(samples) ;

    }

    public SampleDTO toDTO(Sample sample) {
        SampleDTO dto = new SampleDTO();
        dto.setSampleId(sample.getSampleId());
        dto.setParticipantId(sample.getParticipant().getParticipantId());
        dto.setSampleType(sample.getSampleType());
        dto.setCollectionDateTime(sample.getCollectionDateTime());
        dto.setQuality(sample.getQuality());
        dto.setStatus(sample.getStatus());
        dto.setResult(sample.getResult());
        dto.setNotes(sample.getNotes());

        Participant p = sample.getParticipant();
        if (p != null) {
            dto.setParticipantCitizenId(p.getCitizenId());

            dto.setParticipantName(p.getName());
            dto.setGender(p.getGender().name());
        }

        return dto;
    }



}