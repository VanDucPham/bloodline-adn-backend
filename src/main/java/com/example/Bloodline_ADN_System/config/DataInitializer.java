package com.example.Bloodline_ADN_System.config;

import com.example.Bloodline_ADN_System.Entity.Blog;
import com.example.Bloodline_ADN_System.Entity.Service;
import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.repository.BlogRepository;
import com.example.Bloodline_ADN_System.repository.ServiceRepository;
import com.example.Bloodline_ADN_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🔍 DataInitializer đang chạy...");
        System.out.println("📊 Số lượng user hiện tại: " + userRepository.count());
        System.out.println("📊 Số lượng service hiện tại: " + serviceRepository.count());
        System.out.println("📊 Số lượng blog hiện tại: " + blogRepository.count());
        
        // Chỉ chạy nếu chưa có dữ liệu
        if (userRepository.count() == 0) {
            System.out.println("🚀 Bắt đầu tạo dữ liệu mẫu...");
            initializeData();
        } else {
            System.out.println("✅ Database đã có dữ liệu, bỏ qua việc tạo mẫu");
        }
    }

    public void initializeData() {
        // Tạo admin user
        User admin = new User();
        admin.setEmail("admin@bloodline.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setName("Admin");
        admin.setPhone("0123456789");
        admin.setAddress("Hà Nội");
        admin.setRole(User.UserRole.ADMIN);
        admin.setStatus(User.Status.ACTIVE);
        admin.setCreateAt(LocalDate.now());
        admin.setBirthDate(LocalDate.of(1990, 1, 1));
        userRepository.save(admin);

        // Tạo customer user
        User customer = new User();
        customer.setEmail("customer@example.com");
        customer.setPassword(passwordEncoder.encode("customer123"));
        customer.setName("Nguyễn Văn A");
        customer.setPhone("0987654321");
        customer.setAddress("TP.HCM");
        customer.setRole(User.UserRole.CUSTOMER);
        customer.setStatus(User.Status.ACTIVE);
        customer.setCreateAt(LocalDate.now());
        customer.setBirthDate(LocalDate.of(1995, 5, 15));
        userRepository.save(customer);

        // Tạo services
        Service service1 = new Service();
        service1.setServiceName("Xét nghiệm ADN cha con");
        service1.setServiceDescription("Xét nghiệm ADN để xác định mối quan hệ cha con với độ chính xác 99.9%");
        service1.setLimitPeople(100);
        service1.setServicePrice(2000000.0);
        service1.setImageUrl("https://example.com/dna-test.jpg");
        serviceRepository.save(service1);

        Service service2 = new Service();
        service2.setServiceName("Xét nghiệm ADN mẹ con");
        service2.setServiceDescription("Xét nghiệm ADN để xác định mối quan hệ mẹ con với độ chính xác cao");
        service2.setLimitPeople(80);
        service2.setServicePrice(1800000.0);
        service2.setImageUrl("https://example.com/mother-child-dna.jpg");
        serviceRepository.save(service2);

        Service service3 = new Service();
        service3.setServiceName("Xét nghiệm ADN anh em");
        service3.setServiceDescription("Xét nghiệm ADN để xác định mối quan hệ anh em ruột");
        service3.setLimitPeople(60);
        service3.setServicePrice(1500000.0);
        service3.setImageUrl("https://example.com/sibling-dna.jpg");
        serviceRepository.save(service3);

        // Tạo blogs
        Blog blog1 = new Blog();
        blog1.setAuthor(admin);
        blog1.setTitle("Xét nghiệm ADN - Giải pháp khoa học cho việc xác định quan hệ huyết thống");
        blog1.setContent("Xét nghiệm ADN là phương pháp khoa học hiện đại nhất để xác định mối quan hệ huyết thống giữa các cá nhân. Với độ chính xác lên đến 99.9%, đây là công cụ không thể thiếu trong việc giải quyết các vấn đề về quan hệ gia đình.");
        blog1.setImageUrl("https://example.com/blog1.jpg");
        blog1.setStatus(Blog.BlogStatus.PUBLISHED);
        blog1.setBlogType(Blog.BlogType.GUIDE);
        blog1.setCreatedAt(LocalDateTime.now());
        blog1.setPublishDate(LocalDateTime.now());
        blogRepository.save(blog1);

        Blog blog2 = new Blog();
        blog2.setAuthor(admin);
        blog2.setTitle("Quy trình xét nghiệm ADN tại Bloodline ADN");
        blog2.setContent("Tại Bloodline ADN, chúng tôi tuân thủ quy trình xét nghiệm ADN nghiêm ngặt theo tiêu chuẩn quốc tế. Từ việc thu thập mẫu đến phân tích kết quả, mọi bước đều được thực hiện với sự cẩn thận tối đa.");
        blog2.setImageUrl("https://example.com/blog2.jpg");
        blog2.setStatus(Blog.BlogStatus.PUBLISHED);
        blog2.setBlogType(Blog.BlogType.GUIDE);
        blog2.setCreatedAt(LocalDateTime.now());
        blog2.setPublishDate(LocalDateTime.now());
        blogRepository.save(blog2);

        Blog blog3 = new Blog();
        blog3.setAuthor(admin);
        blog3.setTitle("Những điều cần biết trước khi làm xét nghiệm ADN");
        blog3.setContent("Trước khi thực hiện xét nghiệm ADN, bạn cần hiểu rõ về quy trình, thời gian chờ kết quả, và các yếu tố có thể ảnh hưởng đến độ chính xác của kết quả.");
        blog3.setImageUrl("https://example.com/blog3.jpg");
        blog3.setStatus(Blog.BlogStatus.PUBLISHED);
        blog3.setBlogType(Blog.BlogType.NEWS);
        blog3.setCreatedAt(LocalDateTime.now());
        blog3.setPublishDate(LocalDateTime.now());
        blogRepository.save(blog3);

        System.out.println("✅ Dữ liệu mẫu đã được tạo thành công!");
    }
} 