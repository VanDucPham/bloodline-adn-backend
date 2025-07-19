package com.example.Bloodline_ADN_System.repository;


import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.request.AppoinmentAsignedStaffDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("Select u from User u where u.role ='STAFF'")
    List<User> getAllIsStaff();

    User findByStaffCode(String staffCode);

    // Thêm method để tìm user bằng email (sử dụng làm username)
    default Optional<User> findByUsername(String username) {
        return findByEmail(username);
    }
}
