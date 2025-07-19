package com.example.Bloodline_ADN_System.controller;

import com.example.Bloodline_ADN_System.dto.noneWhere.BlogDTO;
import com.example.Bloodline_ADN_System.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog")
@AllArgsConstructor
public class BlogController {
    private final BlogService blogService;

    // Lấy tất cả blog đã xuất bản
    @GetMapping("/all")
    public ResponseEntity<?> getAllPublishedBlogs() {
        try {
            List<BlogDTO> blogs = blogService.getAllBlogDTO().stream()
                    .filter(b -> "PUBLISHED".equals(b.getStatus()))
                    .toList();
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách blog: " + e.getMessage());
        }
    }

    // Lấy các blog liên quan (ĐẶT TRƯỚC endpoint /{id} để tránh mapping nhầm)
    @GetMapping("/related")
    public ResponseEntity<?> getRelatedBlogs(
        @RequestParam Long blogId,
        @RequestParam(defaultValue = "4") int limit
    ) {
        try {
            if (blogId == null || blogId <= 0) {
                return ResponseEntity.badRequest().body("BlogId không hợp lệ");
            }
            if (limit <= 0 || limit > 20) {
                return ResponseEntity.badRequest().body("Limit phải từ 1-20");
            }
            return ResponseEntity.ok(blogService.getRelatedBlogs(blogId, limit));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi lấy blog liên quan: " + e.getMessage());
        }
    }

    // Lấy chi tiết blog đã xuất bản
    @GetMapping("/{id}")
    public ResponseEntity<?> getPublishedBlogById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("ID blog không hợp lệ");
            }
            
            Optional<BlogDTO> blogOpt = blogService.getBlogById(id);
            if (blogOpt.isPresent() && "PUBLISHED".equals(blogOpt.get().getStatus())) {
                return ResponseEntity.ok(blogOpt.get());
            } else {
                return ResponseEntity.status(404).body("Blog không tồn tại hoặc chưa được xuất bản");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi lấy chi tiết blog: " + e.getMessage());
        }
    }

    // Phân trang blog đã xuất bản
    @GetMapping("/page")
    public ResponseEntity<?> getPublishedBlogsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            if (page < 0) {
                return ResponseEntity.badRequest().body("Số trang phải >= 0");
            }
            if (size <= 0 || size > 100) {
                return ResponseEntity.badRequest().body("Kích thước trang phải từ 1-100");
            }
            
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
            Page<BlogDTO> all = blogService.getBlogsPage(pageable, "PUBLISHED", null, null);
            return ResponseEntity.ok(all);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi lấy danh sách blog phân trang: " + e.getMessage());
        }
    }

    // Lấy số lượng blog theo loại (public)
    @GetMapping("/category-count")
    public ResponseEntity<?> getBlogCategoryCount() {
        try {
            return ResponseEntity.ok(blogService.getBlogCategoryCount());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi khi lấy số lượng blog theo danh mục: " + e.getMessage());
        }
    }
} 