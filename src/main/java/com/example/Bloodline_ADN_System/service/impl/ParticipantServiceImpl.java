package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.Appointment;
import com.example.Bloodline_ADN_System.Entity.Participant;
import com.example.Bloodline_ADN_System.Entity.Sample;
import com.example.Bloodline_ADN_System.dto.noneWhere.ParticipantResponeDTO;
import com.example.Bloodline_ADN_System.dto.TrackingAppoint.UpdateParticipant;
import com.example.Bloodline_ADN_System.dto.TrackingAppoint.UpdateSample;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.ParticipantDTO;
import com.example.Bloodline_ADN_System.repository.AppointmentRepository;
import com.example.Bloodline_ADN_System.repository.ParticipantRepository;
import com.example.Bloodline_ADN_System.repository.SampleRepository;
import com.example.Bloodline_ADN_System.service.ParticipantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final AppointmentRepository appointmentRepository;
    private final ParticipantRepository participantRepository;
    private final SampleRepository sampleRepository;

    public ParticipantServiceImpl(AppointmentRepository appointmentRepository,
                                  ParticipantRepository participantRepository, SampleRepository sampleRepository) {
        this.appointmentRepository = appointmentRepository;
        this.participantRepository = participantRepository;
        this.sampleRepository = sampleRepository;
    }

    //Tạo danh sách người tham gia xét nghiệm
    @Override
    public List<ParticipantDTO> addParticipant(List<ParticipantDTO> participantDTOList) {
        return participantDTOList.stream()
                .map(dto -> {
                    Appointment appointment = appointmentRepository.findById(dto.getAppointmentId())
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy appointment"));
                    try {
                        Participant participant = new Participant();
                        participant.setName(dto.getName());
                        participant.setRelationship(dto.getRelationship());
                        participant.setGender(Participant.Gender.valueOf(dto.getGender()));
                        participant.setCitizenId(dto.getCitizenId());
                        participant.setAddress(dto.getAddress());
                        participant.setBirthDate(dto.getBirthDate());
                        participant.setAppointment(appointment);

                        Participant saved = participantRepository.save(participant);
                        return toDTO(saved);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        return null; // or handle as needed
                    }
                })
                .filter(dto -> dto != null) // remove failed items
                .collect(Collectors.toList());
    }

    //Lấy danh sách participant từ appointmentId
    public List<ParticipantResponeDTO> getParticipantByAppointmentId(Long appointmentId) {
        List<Participant> participants = participantRepository.findByAppointment_AppointmentId(appointmentId);

        // Chuyển sang DTO
        return participants.stream()
                .map(dto ->{
                    ParticipantResponeDTO participantDTO = new ParticipantResponeDTO();
                    participantDTO.setParticipantId(dto.getParticipantId());
                    participantDTO.setName(dto.getName());
                    participantDTO.setRelationship(dto.getRelationship());
                    participantDTO.setGender(dto.getGender());
                    participantDTO.setCitizenId(dto.getCitizenId());
                    participantDTO.setAddress(dto.getAddress());
                    participantDTO.setBirthDate(dto.getBirthDate());
                    participantDTO.setAppointmentId(dto.getAppointment().getAppointmentId());
                    return participantDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Participant> saveAll(List<Participant> participants) {

        return participantRepository.saveAll(participants);
    }

    @Override
    public boolean updateParticipant(Long id, UpdateParticipant dto) {
        Optional<Participant> optional = participantRepository.findById(id);
        if (optional.isEmpty()) return false;

        Participant participant = optional.get();

        // Cập nhật thông tin người tham gia
        participant.setName(dto.getName());
        participant.setCitizenId(dto.getCitizenId());
        participant.setRelationship(dto.getRelationShip());

        // Cập nhật mẫu nếu có
        UpdateSample sampleDTO = dto.getSample();
        if (sampleDTO != null) {
            Sample sample = participant.getSample();

            // Nếu chưa có sample, tạo mới
            if (sample == null) {
                sample = new Sample();
                sample.setParticipant(participant);
            }

            // Cập nhật loại mẫu
            if (sampleDTO.getSampleType() != null) {
                sample.setSampleType(sampleDTO.getSampleType());
            }

            // Nếu bạn có thêm trường như status/result/etc, có thể set thêm ở đây

            sampleRepository.save(sample);
            participant.setSample(sample);
        }

        participantRepository.save(participant);
        return true;
    }


    private ParticipantDTO toDTO(Participant participant) {
        ParticipantDTO dto = new ParticipantDTO();
        dto.setName(participant.getName());
        dto.setRelationship(participant.getRelationship());
        dto.setGender(String.valueOf(participant.getGender()));
        dto.setCitizenId(participant.getCitizenId());
        dto.setAddress(participant.getAddress());
        dto.setBirthDate(participant.getBirthDate());
        dto.setAppointmentId(participant.getAppointment().getAppointmentId());
        return dto;
    }
}