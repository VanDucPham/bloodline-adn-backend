package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.*;
import com.example.Bloodline_ADN_System.dto.PaymentDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.ApiMessResponse;
import com.example.Bloodline_ADN_System.dto.noneWhere.SampleDTO;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.*;
import com.example.Bloodline_ADN_System.payment.PaymentRequest;
import com.example.Bloodline_ADN_System.repository.*;
import com.example.Bloodline_ADN_System.service.*;
import com.example.Bloodline_ADN_System.service.AllowedAreaService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
@org.springframework.stereotype.Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final SampleService sampleService;
    private final ParticipantService participantService;
    private final CaseFileService caseFileService;
    private final TimeSlotLimitRepository timeSlotLimitRepository;
    private final ServiceRepository serviceRepository;
    private final PaymentService paymentService;
    @Autowired
    private AllowedAreaService allowedAreaService;


    // ---------------------CREATE APPOINTMENT FOR CASEFILE---------------------
    @Override
    public AppointmentResponse<AppointmentDTO> createAppointmentCaseFile(AppointmentDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        Service service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại"));

        Appointment appointment = new Appointment();
        //nếu có toEntity thì không cần set thủ công như thế này
        appointment.setUser(user);
        appointment.setService(service);
        appointment.setType(dto.getAppointmentType());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setDeliveryMethod(Appointment.DeliveryMethod.valueOf(dto.getDeliveryMethod()));
        appointment.setAppointmentNote(dto.getAppointmentNote());
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        appointment.setCollectionStatus(Appointment.CollectionStatus.ASSIGNED);

        Appointment saved = appointmentRepository.save(appointment);
        return new AppointmentResponse<>("Đặt lịch thành công", toDTO(saved));
    }

    @Override
    public AppointmentResponse<AppointmentDTO> createAppointmentByStaff(AppointmentRequest request) {
        // 1. Save Case File
        caseFileDTO caseFiledto = request.getCaseFile();
        System.out.println(request.getCaseFile().getUserId());
        System.out.println(caseFiledto.getUserId());
        userRepository.findAll().forEach(System.out::println);
        System.out.println("userId: " + caseFiledto.getUserId() + ", type: " + caseFiledto.getUserId().getClass());
        Optional<User> test = userRepository.findById(6L);
        System.out.println("Found? " + test.isPresent());

        String caseCodegen = caseFileService.generateCaseCode(String.valueOf(caseFiledto.getCaseType())) ;
        caseFiledto.setCaseCode(caseCodegen);
        CaseFile caseFile = new CaseFile();
        caseFile  = caseFileService.createCaseFile(caseFiledto);

        // 2. Validate
        AppointmentDTO dto = request.getAppointment();
        validateAppointmentDate(dto.getAppointmentDate(), dto.getDeliveryMethod());
        validateSlotAvailability(dto.getAppointmentDate(), dto.getAppointmentTime());



        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        Service service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Dịch vụ không tồn tại"));

        Appointment appointment = new Appointment();
        //nếu có toEntity thì không cần set thủ công như thế này
        appointment.setUser(user);
        appointment.setService(service);
        appointment.setType(dto.getAppointmentType());
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setAppointmentTime(LocalTime.now());
        appointment.setDeliveryMethod(Appointment.DeliveryMethod.valueOf(dto.getDeliveryMethod()));
        appointment.setAppointmentNote(dto.getAppointmentNote());
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        appointment.setCollectionStatus(Appointment.CollectionStatus.ASSIGNED);
        appointment.setCaseFile(caseFile);
        appointment.setAssignedStaff(user);

        Appointment saved = appointmentRepository.save(appointment);
        return new AppointmentResponse<>("Đặt lịch thành công", toDTO(saved));
    }

    private void validateAppointmentDate(LocalDate date, String deliveryMethod) {
        // For home collection/delivery, date can be null
        if (date == null && ("HOME_COLLECTION".equals(deliveryMethod) || "HOME_DELIVERY".equals(deliveryMethod))) {
            return; // Skip validation for home collection/delivery without specific date
        }
        // For facility appointments or when date is provided, validate it
        if (date != null) {
            LocalDate today = LocalDate.now();
            if (!date.isAfter(today)) {
                throw new IllegalArgumentException("Lịch hẹn phải được đặt trước ít nhất 1 ngày.");
            }
        } else {
            throw new IllegalArgumentException("Ngày hẹn không được để trống cho lịch hẹn tại cơ sở.");
        }
    }

    private void validateSlotAvailability(LocalDate date, LocalTime time) {
        if (time == null || date == null) {
            // Không có thời gian, không kiểm tra slot
            return;
        }

        if (date == null) {
            // Có thời gian nhưng không có ngày => lỗi
            throw new IllegalArgumentException("Ngày hẹn không được để trống nếu có giờ hẹn.");
        }

        int count = appointmentRepository.countByAppointmentDateAndAppointmentTime(date, time);
        if (count >= 5) {
            throw new IllegalArgumentException("Khung giờ đã đầy. Vui lòng chọn khung giờ khác.");
        }
    }




    // --------------------- CUSTOMER CREATE FULL APPOINTMENT --------
    // -------------



    @Override
    @Transactional
    public AppointmentResponse<AppointmentDTO> createAppointment(AppointmentRequest request) {
        // 1. Save Case File
        caseFileDTO caseFiledto = request.getCaseFile();
        String caseCodegen = caseFileService.generateCaseCode(String.valueOf(caseFiledto.getCaseType())) ;
        caseFiledto.setCaseCode(caseCodegen);
        CaseFile caseFile = new CaseFile();
         caseFile  = caseFileService.createCaseFile(caseFiledto);


        // 2. Validate
        AppointmentDTO dto = request.getAppointment();
        validateAppointmentDate(dto.getAppointmentDate(), dto.getDeliveryMethod());
        validateSlotAvailability(dto.getAppointmentDate(), dto.getAppointmentTime());

        // --- Kiểm tra khu vực lấy mẫu tại nhà ---
        if ("HOME_COLLECTION".equals(dto.getAppointmentType())) {
            String city = dto.getCollectionCity(); // Giả sử có trường city trong DTO
            String district = dto.getCollectionDistrict(); // Giả sử có trường district trong DTO
            if (city == null || city.trim().isEmpty() || district == null || district.trim().isEmpty()) {
                throw new RuntimeException("Vui lòng chọn đầy đủ thành phố và quận/huyện lấy mẫu tại nhà.");
            }
            if (!allowedAreaService.isAllowed(city, district)) {
                throw new RuntimeException("Khu vực này chưa hỗ trợ lấy mẫu tại nhà. Vui lòng chọn khu vực khác.");
            }
        }
        // --- End kiểm tra khu vực ---

        // 3. Create & Save Appointment


        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + dto.getUserId()));



        com.example.Bloodline_ADN_System.Entity.Service service = serviceRepository.findById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ với ID: " + dto.getServiceId()));

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setService(service);
        appointment.setType(dto.getAppointmentType());
        if(dto.getAppointmentDate() != null) {
            appointment.setAppointmentDate(dto.getAppointmentDate());
        }

        appointment.setCollectionAddress(dto.getCollectionAddress());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setDeliveryMethod(Appointment.DeliveryMethod.valueOf(dto.getDeliveryMethod()));
        appointment.setAppointmentNote(dto.getAppointmentNote());
        appointment.setStatus(Appointment.AppointmentStatus.SCHEDULED);
        appointment.setCaseFile(caseFile);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        AppointmentDTO savedDto = toDTO(savedAppointment);

        PaymentRequest paymentDto = request.getPayment();
        if ((paymentDto!= null )){
            paymentService.createPayment(paymentDto, savedAppointment.getAppointmentId()) ;
        }

// 4. Save Participants
        List<Participant> participants = request.getParticipants().stream()
                .map(dto1 -> dto1.toEntity(savedAppointment))
                .collect(Collectors.toList());

        List<Participant> savedParticipants = participantService.saveAll(participants);

        // ✅ Tạo lại map từ danh sách đã lưu, có ID đầy đủ
        Map<String, Participant> participantMap = savedParticipants.stream()
                .collect(Collectors.toMap(Participant::getCitizenId, p -> p));
        // 5. Save Samples
        List<SampleDTO> sampleDTOs = request.getSamples();

        if (sampleDTOs != null ) {
            List<Sample> samples = sampleDTOs.stream()
                    .map(sampleDto -> {
                        Participant participant = participantMap.get(sampleDto.getParticipantCitizenId());
                        if (participant == null) {
                            throw new RuntimeException("Không tìm thấy participant với CCCD: " + sampleDto.getParticipantCitizenId());
                        }

                        Sample sample = new Sample();
                        sample.setParticipant(participant);
                        sample.setSampleType(sampleDto.getSampleType());
                        sample.setCollectionDateTime(sampleDto.getCollectionDateTime());
                        sample.setQuality(sampleDto.getQuality());
                        sample.setStatus(sampleDto.getStatus());
                        sample.setResult(sampleDto.getResult());
                        sample.setNotes(sampleDto.getNotes());
                        return sample;
                    })
                    .toList();
            sampleService.saveAll(samples);
        }

        // 6. Build Response
        AppointmentDTO responseDto = toDTO(savedAppointment);
        responseDto.setCaseCode(caseFile.getCaseCode());
        responseDto.setParticipants(savedParticipants.stream().map(this::toParticipantDTO).toList());

        if(sampleDTOs != null && !sampleDTOs.isEmpty()){
            responseDto.setSamples(request.getSamples().stream().map(this::toSampleDTO).toList());
        }


        return new AppointmentResponse<>("Tạo lịch hẹn thành công!", responseDto);
    }


    private ParticipantDTO toParticipantDTO(Participant p) {
        return new ParticipantDTO(p.getParticipantId(), p.getName(), p.getRelationship(), p.getCitizenId(),
                p.getAddress(), p.getBirthDate(), p.getGender());
    }

    private SampleDTO toSampleDTO(SampleDTO s) {
        return new SampleDTO(s.getSampleId(), s.getParticipantId(), s.getParticipantCitizenId(),
                s.getParticipantName(),s.getGender() ,s.getSampleType(), s.getCollectionDateTime(),
                s.getQuality(), s.getStatus(), s.getResult(), s.getNotes());
    }

    @Override
    public boolean checkAvailability(LocalDate date, LocalTime time, String email) {
        if (date.isBefore(LocalDate.now()) || (date.isEqual(LocalDate.now()) && time.isBefore(LocalTime.now()))) {
            return false;
        }

        Optional<TimeSlotLimit> slotOpt = timeSlotLimitRepository.findByStartTimeLessThanEqualAndEndTimeGreaterThanEqual(time, time);
        if (slotOpt.isEmpty()) return false;

        TimeSlotLimit slot = slotOpt.get();
        int currentAppointments = appointmentRepository.countByAppointmentDateAndAppointmentTimeBetween(date, slot.getStartTime(), slot.getEndTime());
        if (currentAppointments >= slot.getMaxAppointments()) return false;

        Long userId = getUserIdByUsername(email);
        return !appointmentRepository.existsByUser_UserIdAndAppointmentDateAndAppointmentTimeBetween(
                userId, date, slot.getStartTime(), slot.getEndTime());
    }

    @Override
    public Long getUserIdByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"))
                .getUserId();
    }

    @Override
    public boolean isUserIdExist(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Long findCaseIdByAppointmentId(Long id) {

        return appointmentRepository.findCaseIdByAppointmentId(id) ;
    }

    @Override
    public List<AppointmentDTO> getAllAppointment() {
        return appointmentRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<AppointmentDTO> getAppointmentByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Người dùng không tồn tại");
        }
        return appointmentRepository.findByUserUserId(userId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ApiMessResponse cancelAppointment(Long id) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(id);
        if (optionalAppointment.isEmpty()) {
            return new ApiMessResponse(false, "Không tìm thấy lịch hẹn.");
        }

        Appointment appointment = optionalAppointment.get();

        if (appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            return new ApiMessResponse(false, "Lịch hẹn đã bị hủy trước đó.");
        }

        if (appointment.getStatus() != Appointment.AppointmentStatus.COMPLETED
                && appointment.getStatus() != Appointment.AppointmentStatus.SCHEDULED) {
            return new ApiMessResponse(false, "Chỉ có thể hủy lịch hẹn đã xác nhận (COMPLETED) hoặc đang chờ (SCHEDULED).");
        }


        // Kiểm tra ngày hiện tại cách ngày hẹn < 2 ngày
        LocalDate appointmentDate = appointment.getAppointmentDate(); // Giả sử kiểu LocalDate
        LocalDate today = LocalDate.now();
        long daysBetween = ChronoUnit.DAYS.between(today, appointmentDate);

        if (daysBetween < 2) {
            return new ApiMessResponse(false, "Không thể hủy lịch hẹn trước 2 ngày.");
        }

        // Hủy thành công
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        return new ApiMessResponse(true, "Hủy lịch hẹn thành công.");
    }



    @Override
    public List<AppointmentDTO> filterAppointment(Appointment.AppointmentStatus status,
                                                  Appointment.AppointmentType type,
                                                  LocalDate date) {
        return appointmentRepository.findByFilters(status, type, date)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDTO updateAppointmentProgress(Long id, Appointment.AppointmentStatus status, Appointment.CollectionStatus collectionStatus) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lịch hẹn không tồn tại!"));

        appointment.setStatus(status);
        appointment.setCollectionStatus(collectionStatus);
        return toDTO(appointmentRepository.save(appointment));
    }


    @Override
    public void updateAppointment(AppointmentDTO appointmentDTO, Long appointmentId) {
        // Lấy thông tin lịch hẹn hiện tại
        Appointment appointment = appointmentRepository.getAppointmentsByAppointmentId(appointmentId);

        // Cập nhật các trường từ DTO
        if (appointmentDTO.getAppointmentDate() != null)
            appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());

        if (appointmentDTO.getAppointmentTime() != null)
            appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());

        if (appointmentDTO.getAppointmentNote() != null)
            appointment.setAppointmentNote(appointmentDTO.getAppointmentNote());

        if (appointmentDTO.getCollectionAddress() != null)
            appointment.setCollectionAddress(appointmentDTO.getCollectionAddress());

        if (appointmentDTO.getDeliveryMethod() != null)
            appointment.setDeliveryMethod(Appointment.DeliveryMethod.valueOf(appointmentDTO.getDeliveryMethod()));

        // Lưu lại
        appointmentRepository.save(appointment);
    }

    public AppointmentDTO getAppointmentById(Long id) {
        return toDTO(appointmentRepository.getAppointmentsByAppointmentId(id));
    }



    public AppointmentDTO toDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setAppointmentId(appointment.getAppointmentId());
        dto.setUserId(appointment.getUser().getUserId());
        dto.setServiceId(appointment.getService().getServiceId());
        dto.setAppointmentType(appointment.getType());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setAppointmentStatus(appointment.getStatus());
        dto.setDeliveryMethod(String.valueOf(appointment.getDeliveryMethod()));
        dto.setAppointmentNote(appointment.getAppointmentNote());
        dto.setCollectionStatus(appointment.getCollectionStatus());
        dto.setAssignedStaff(appointment.getUser().getUserId());
        return dto;
    }
}


