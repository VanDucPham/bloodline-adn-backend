package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.Entity.CaseFile;
import com.example.Bloodline_ADN_System.dto.managerCaseFile.caseFileDTO;

import java.util.List;
import java.util.Optional;

public interface CaseFileService {

    CaseFile  createCaseFile(caseFileDTO req);




    List<CaseFile> findAll() ;
    CaseFile findById(Long customerId);

    List<CaseFile> findByFileName(String fileName);
    List<CaseFile> findByFileType(String fileType);
    public String generateCaseCode(String type) ;

    <Optional> CaseFile findByCaseCode(String caseCode);

    Optional<CaseFile> findByCaseCode(Long caseId);
}
