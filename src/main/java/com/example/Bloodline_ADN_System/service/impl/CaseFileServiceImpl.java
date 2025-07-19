package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.CaseFile;
import com.example.Bloodline_ADN_System.Entity.Service;
import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.caseFileDTO;
import com.example.Bloodline_ADN_System.repository.CaseFileRepository;
import com.example.Bloodline_ADN_System.repository.ServiceRepository;
import com.example.Bloodline_ADN_System.service.AppointmentService;
import com.example.Bloodline_ADN_System.service.CaseFileService;
import com.example.Bloodline_ADN_System.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CaseFileServiceImpl implements CaseFileService {
    private final UserService userService;
    private final CaseFileRepository caseFileRepository;
    private final ServiceRepository serviceRepository;


    public CaseFileServiceImpl(UserService userService,
                               CaseFileRepository caseFileRepository,
                               ServiceRepository serviceRepository) {
        this.userService = userService;
        this.caseFileRepository = caseFileRepository;
        this.serviceRepository = serviceRepository;

    }


    @Override
    public CaseFile createCaseFile(caseFileDTO req) {
        // Lấy user từ ID
        User user = userService.findUserById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + req.getUserId()));
        Service service = serviceRepository.findById(req.getServiceId()).orElseThrow(()->new RuntimeException("Không timfg thấy dichj vụ với ID" + req.getServiceId()));


        // Tạo mới CaseFile
        CaseFile caseFile = new CaseFile();
        caseFile.setCaseCode(req.getCaseCode());
        caseFile.setCaseType(CaseFile.CaseType.valueOf(req.getCaseType()));
        caseFile.setService(service);
        caseFile.setCreatedAt(LocalDateTime.now());

        // Gán user vào caseFile
        caseFile.setCreatedBy(user);

        // Lưu vào DB
        caseFileRepository.save(caseFile);

        return caseFile;
    }


    @Override
    public List<CaseFile> findAll() {
        return caseFileRepository.findAll();
    }

    @Override
    public CaseFile findById(Long id) {



        return caseFileRepository.findById(id).orElse(null);
    }

    @Override
    public List<CaseFile> findByFileName(String fileName) {
        return List.of();
    }

    @Override
    public List<CaseFile> findByFileType(String fileType) {
        return List.of();
    }

    @Override
    public String generateCaseCode(String type) {
        String prefix = switch (type.toUpperCase()){
            case "ADMINISTRATIVE" -> "HC";
            case "CIVIL" -> "DS";
            default -> "N";
        } ;
        String caseCode ;
        int retry = 0 ;
        do{
            String timsemap = String.valueOf(System.currentTimeMillis()).substring(7);
            caseCode = prefix + timsemap;
            retry++;
            if(retry> 5){
                throw new RuntimeException("Không thế tạo mã hồ sơ duy nhaats sau 5 lần thử");
            }
        }while(caseFileRepository.existsCaseFilesByCaseCode(caseCode))  ;


        return caseCode;
    }

    @Override
    public <Optional> CaseFile findByCaseCode(String caseCode) {
        return null;
    }

    @Override
    public Optional<CaseFile> findByCaseCode(Long caseId) {


        return  caseFileRepository.findById(caseId);
    }


}
