package com.example.Bloodline_ADN_System.repository;

import com.example.Bloodline_ADN_System.Entity.CaseFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaseFileRepository extends JpaRepository<CaseFile, Long> {
    boolean existsCaseFilesByCaseCode(String caseCode);

    CaseFile findByCaseCode(String caseCode);
    
    // Đếm số lượng case file theo service ID
    long countByService_ServiceId(Long serviceId);
}
