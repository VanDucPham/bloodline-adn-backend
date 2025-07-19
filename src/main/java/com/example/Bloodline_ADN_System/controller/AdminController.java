package com.example.Bloodline_ADN_System.controller;

//import com.example.Bloodline_ADN_System.Entity.Blog;
//import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.dto.*;
import com.example.Bloodline_ADN_System.dto.ManagerService.ServiceManagerDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.StaffDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.request.AppoinmentAsignedStaffDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.request.AssignmentRequestDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.response.CaseassignmentDTO;
import com.example.Bloodline_ADN_System.dto.ScheduleManager.response.DayscheduleDTO;
import com.example.Bloodline_ADN_System.dto.noneWhere.*;
import com.example.Bloodline_ADN_System.repository.UserRepository;
import com.example.Bloodline_ADN_System.service.BlogService;
import com.example.Bloodline_ADN_System.service.ScheduleService;
import com.example.Bloodline_ADN_System.service.ServiceList;
import com.example.Bloodline_ADN_System.service.UserService;
import com.example.Bloodline_ADN_System.service.impl.AdminServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
//import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;



import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final AdminServiceImpl adminService;
    private final UserRepository userRepository;
    private final ServiceList serviceList;
    private final BlogService blogService;
    private final UserService userService;
    private final ScheduleService scheduleService;


    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
        adminService.createUser(request);
        return ResponseEntity.ok("Tạo " + request.getRole() + " thành công");
    }

    @GetMapping("/alluser")
    public ResponseEntity<List<accountResponse>> getAllUser() {
        List<accountResponse> accountResponse = adminService.getAllUsers();
        System.out.println(accountResponse.size());
        return ResponseEntity.ok(accountResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody updateUserRequest request) {
        adminService.updateUser(id, request);
        System.out.println("Hello");
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }
    // Controller
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("Xóa người dùng thành công");
    }
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        boolean exists = userRepository.existsByEmail(email);
        System.out.println("có mail rồi");
        return ResponseEntity.ok(exists);
    }
    @PostMapping(
            path = "/import_user",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> importUser(@RequestParam("file") MultipartFile file) {
        try {
            adminService.importUserFromExcel(file);
            return ResponseEntity.ok("Import thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi import file: " + e.getMessage());
        }
    }

    @GetMapping("/service/get")
    public ResponseEntity<List<ServiceManagerDTO>> getAllServices() {
        List<ServiceManagerDTO> service = serviceList.getAllServices();
        return ResponseEntity.ok(service);
    }

    @GetMapping("/service/get/{id}")
    public ResponseEntity<ServiceManagerDTO> getServiceById(@PathVariable Long id) {
        ServiceManagerDTO service = serviceList.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @PostMapping("/service/add")
    public ResponseEntity<ServiceManagerDTO> addService(@RequestBody ServiceManagerDTO serviceDTO) {
        return ResponseEntity.ok(serviceList.createService(serviceDTO));
    }

    @PutMapping("/service/update/{id}")
    public ResponseEntity<ServiceManagerDTO> updateService(@PathVariable Long id,@RequestBody ServiceManagerDTO serviceDTO){
        System.out.println("dang update");
        return ResponseEntity.ok(serviceList.updateService(id,serviceDTO));
    }

    @DeleteMapping("/service/delete/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Long id) {
        try {
            serviceList.deleteService(id);
            return ResponseEntity.ok("Xóa dịch vụ thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi xóa dịch vụ: " + e.getMessage());
        }
    }

    @GetMapping ("/profile")
    public ResponseEntity<UserUpdateDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping("/blog/create")
    public ResponseEntity<BlogDTO> createBlog(@RequestBody BlogDTO dto) {
        return ResponseEntity.ok(blogService.createBlog(dto));
    }

    @GetMapping("/blog/all")
    public List<BlogDTO> getAllBlogs() {
        return blogService.getAllBlogDTO();
    }

    // Lấy blog theo id, trả về DTO
    @GetMapping("/blog/get/{id}")
    public Optional<BlogDTO> getBlogById(@PathVariable Long id) {
        return blogService.getBlogById(id);
    }

    // Xóa blog
    @DeleteMapping("/blog/delete/{id}")
    public void deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
    }

    // Cập nhật blog từ DTO
    @PutMapping("/blog/update/{id}")
    public BlogDTO updateBlog(@PathVariable Long id, @RequestBody BlogDTO dto) {
        return blogService.updateBlog(id, dto);
    }

    @PutMapping("/blog/status/{id}")
    public ResponseEntity<?> updateBlogStatus(@PathVariable Long id, @RequestParam String status) {
        BlogDTO blog = blogService.getBlogById(id)
                .orElseThrow(() -> new RuntimeException("Blog không tồn tại"));
        blog.setStatus(status);
        BlogDTO updated = blogService.updateBlog(id, blog);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/blog/page")
    public ResponseEntity<?> getBlogsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Long authorId
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(blogService.getBlogsPage(pageable, status, type, authorId));
    }
    @GetMapping("/case_schedule/{month}")
    public ResponseEntity<List<DayscheduleDTO>> getCaseSchedule(@PathVariable String  month) {
         List<DayscheduleDTO>   schedule =  scheduleService.getSchedule(month) ;

        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/staff_inf")
    public ResponseEntity<List<StaffDTO>> getStaffInfor() {
        return ResponseEntity.ok(scheduleService.getAllStaff()) ;
    }

    @PostMapping("/assign-staff")
    public ResponseEntity<?> assignStaffToAppointments(@RequestBody List<AppoinmentAsignedStaffDTO> assignments) {
        try {
            scheduleService.assignStaffToCase(assignments);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi phân công");
        }
    }


//
//    @PostMapping("/blog/upload-image")
//    public ResponseEntity<?> uploadBlogImage(@RequestParam("file") MultipartFile file) {
//        try {
//            if (file.isEmpty()) {
//                return ResponseEntity.badRequest().body(Map.of("message", "File rỗng"));
//            }
//            String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown";
//            String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(originalFilename);
//            Path uploadPath = Paths.get("src/main/resources/static/images/blog");
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//            Path filePath = uploadPath.resolve(fileName);
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//            String imageUrl = "/images/blog/" + fileName;
//            return ResponseEntity.ok(Map.of("url", imageUrl));
//        } catch (Exception e) {
//            logger.error("Lỗi upload ảnh: ", e);
//            return ResponseEntity.status(500).body(Map.of("message", "Lỗi upload ảnh: " + e.getMessage()));
//        }
//    }
//
//    private static final String UPLOAD_DIR = "src/main/resources/static/ImagePage/";
//    @PostMapping("/upload")
//    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
//        try {
//            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//            Path uploadPath = Paths.get(UPLOAD_DIR);
//
//            // Tạo thư mục nếu chưa tồn tại
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            Path filePath = uploadPath.resolve(fileName);
//            Files.write(filePath, file.getBytes());
//
//            // URL để trả về client
//            String imageUrl = "/ImagePage/" + fileName;
//            Map<String, String> result = new HashMap<>();
//            result.put("url", imageUrl);
//
//            return ResponseEntity.ok(result);
//        } catch (IOException e) {
//            e.printStackTrace(); // in log lỗi cụ thể
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Collections.singletonMap("error", "Upload failed"));
//        }
//    }

    @GetMapping("/blog/category-count")
    public ResponseEntity<?> getBlogCategoryCount() {
        return ResponseEntity.ok(blogService.getBlogCategoryCount());
    }
}