package com.example.Bloodline_ADN_System.repository;

import com.example.Bloodline_ADN_System.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    // Tính tổng doanh thu trong khoảng thời gian
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate AND p.status = 'COMPLETED'")
    Double sumAmountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // ========================================
    // CÁC METHOD CHO TÍNH TOÁN DOANH THU
    // ========================================
    
    // Đếm số lượng payment completed trong khoảng thời gian
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate AND p.status = 'COMPLETED'")
    Long countCompletedPaymentsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Đếm số lượng payment completed theo service trong khoảng thời gian
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.appointment.service.serviceId = :serviceId " +
           "AND p.paymentDate BETWEEN :startDate AND :endDate AND p.status = 'COMPLETED'")
    Long countCompletedPaymentsByServiceAndDateRange(@Param("serviceId") Long serviceId, 
                                                   @Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);
    
    // Thống kê payment method theo khoảng thời gian
    @Query("SELECT p.paymentMethod, COUNT(p) as count, COALESCE(SUM(p.amount), 0) as total " +
           "FROM Payment p WHERE p.status = 'COMPLETED' " +
           "AND p.paymentDate BETWEEN :startDate AND :endDate " +
           "GROUP BY p.paymentMethod")
    List<Object[]> getPaymentMethodStatsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                                   @Param("endDate") LocalDateTime endDate);
    
    // Top services theo doanh thu trong khoảng thời gian
    @Query("SELECT p.appointment.service.serviceName, COALESCE(SUM(p.amount), 0) as total " +
           "FROM Payment p WHERE p.status = 'COMPLETED' " +
           "AND p.paymentDate BETWEEN :startDate AND :endDate " +
           "GROUP BY p.appointment.service.serviceName " +
           "ORDER BY SUM(p.amount) DESC")
    List<Object[]> getTopServicesByRevenue(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    // Thống kê doanh thu theo tháng
    @Query("SELECT YEAR(p.paymentDate) as year, MONTH(p.paymentDate) as month, COALESCE(SUM(p.amount), 0) as total " +
           "FROM Payment p WHERE p.status = 'COMPLETED' " +
           "GROUP BY YEAR(p.paymentDate), MONTH(p.paymentDate) " +
           "ORDER BY YEAR(p.paymentDate) DESC, MONTH(p.paymentDate) DESC")
    List<Object[]> getRevenueByMonth();
    
    // Thống kê doanh thu theo service
    @Query("SELECT p.appointment.service.serviceName, COALESCE(SUM(p.amount), 0) as total " +
           "FROM Payment p WHERE p.status = 'COMPLETED' " +
           "GROUP BY p.appointment.service.serviceName")
    List<Object[]> getRevenueByService();
    
    // Tính tổng doanh thu theo service trong khoảng thời gian
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
           "WHERE p.appointment.service.serviceId = :serviceId " +
           "AND p.paymentDate BETWEEN :startDate AND :endDate " +
           "AND p.status = 'COMPLETED'")
    Double sumAmountByServiceAndDateRange(@Param("serviceId") Long serviceId, 
                                        @Param("startDate") LocalDateTime startDate, 
                                        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p FROM Payment p WHERE p.appointment.appointmentId = :appointmentId")
    Payment findPaymentByAppointmentId(@Param("appointmentId") Long appointmentId);
    // Thống kê doanh thu theo ngày
} 