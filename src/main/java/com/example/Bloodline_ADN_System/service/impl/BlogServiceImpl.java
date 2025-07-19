package com.example.Bloodline_ADN_System.service.impl;

import com.example.Bloodline_ADN_System.Entity.Blog;
import com.example.Bloodline_ADN_System.Entity.User;
import com.example.Bloodline_ADN_System.dto.noneWhere.BlogDTO;
import com.example.Bloodline_ADN_System.repository.BlogRepository;
import com.example.Bloodline_ADN_System.repository.UserRepository;
import com.example.Bloodline_ADN_System.service.BlogService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BlogServiceImpl implements BlogService {
    @Autowired
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    // Lấy tất cả blog, trả về DTO
    public List<BlogDTO> getAllBlogDTO() {
        return blogRepository.findAllWithAuthor()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // Lấy blog theo id, trả về DTO
    public Optional<BlogDTO> getBlogById(Long id) {
        return blogRepository.findByIdWithAuthor(id)
                .map(this::toDTO);
    }

    //Xóa blog
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    // Validate thủ công BlogDTO
    private void validateBlogDTO(BlogDTO dto) {
        if (dto == null) {
            throw new RuntimeException("Dữ liệu blog không được để trống");
        }
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new RuntimeException("Tiêu đề không được để trống");
        }
        if (dto.getTitle().length() > 255) {
            throw new RuntimeException("Tiêu đề tối đa 255 ký tự");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new RuntimeException("Nội dung không được để trống");
        }
        if (dto.getImageUrl() != null && dto.getImageUrl().length() > 512) {
            throw new RuntimeException("Đường dẫn ảnh tối đa 512 ký tự");
        }
        if (dto.getStatus() != null &&
            !(dto.getStatus().equals("DRAFT") || dto.getStatus().equals("PUBLISHED") || dto.getStatus().equals("ARCHIVED"))) {
            throw new RuntimeException("Trạng thái không hợp lệ (DRAFT, PUBLISHED, ARCHIVED)");
        }
    }

    // Tạo mới blog từ DTO
    public BlogDTO createBlog(BlogDTO dto) {
        validateBlogDTO(dto);
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Blog blog = toEntity(dto, author);
        Blog saved = blogRepository.save(blog);
        return toDTO(saved);
    }

    // Cập nhật blog từ DTO
    public BlogDTO updateBlog(Long id, BlogDTO dto) {
        Blog blog = blogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Blog không tồn tại"));
        // Chỉ cập nhật các trường thực sự muốn đổi
        if (dto.getTitle() != null) blog.setTitle(dto.getTitle());
        if (dto.getContent() != null) blog.setContent(dto.getContent());
        if (dto.getImageUrl() != null) blog.setImageUrl(dto.getImageUrl());
        if (dto.getStatus() != null) blog.setStatus(Blog.BlogStatus.valueOf(dto.getStatus()));
        if (dto.getBlogType() != null) blog.setBlogType(Blog.BlogType.valueOf(dto.getBlogType()));
        if (dto.getAuthorId() != null) {
            User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));
            blog.setAuthor(author);
        }
        Blog updated = blogRepository.save(blog);
        // Lấy lại blog đã update với fetch join author để tránh lỗi lazy loading
        Blog updatedWithAuthor = blogRepository.findByIdWithAuthor(updated.getBlogId())
            .orElseThrow(() -> new RuntimeException("Blog không tồn tại sau update"));
        return toDTO(updatedWithAuthor);
    }

    @Override
    public Page<BlogDTO> getBlogsPage(Pageable pageable, String status, String type, Long authorId) {
        // Lấy toàn bộ blog, fetch join author để tránh lazy loading
        List<Blog> blogs = blogRepository.findAllWithAuthor();
        List<Blog> filtered = blogs.stream()
            .filter(b -> status == null || (b.getStatus() != null && b.getStatus().name().equalsIgnoreCase(status)))
            .filter(b -> type == null || (b.getBlogType() != null && b.getBlogType().name().equalsIgnoreCase(type)))
            .filter(b -> authorId == null || (b.getAuthor() != null && b.getAuthor().getUserId().equals(authorId)))
            .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        List<BlogDTO> content = filtered.subList(start, end > start ? end : start).stream().map(this::toDTO).collect(Collectors.toList());
        return new PageImpl<>(content, pageable, filtered.size());
    }

    public BlogDTO toDTO(Blog blog) {
        BlogDTO dto = new BlogDTO();
        dto.setBlogId(blog.getBlogId());
        if (blog.getAuthor() != null) {
            dto.setAuthorId(blog.getAuthor().getUserId());
            dto.setAuthorName(blog.getAuthor().getName()); // Lấy tên tác giả
        }
        dto.setTitle(blog.getTitle());
        dto.setContent(blog.getContent());
        dto.setImageUrl(blog.getImageUrl());
        dto.setCreatedAt(blog.getCreatedAt());
        dto.setStatus(blog.getStatus() != null ? blog.getStatus().name() : null);
        dto.setBlogType(blog.getBlogType() != null ? blog.getBlogType().name() : null);
        return dto;
    }

    public Blog toEntity(BlogDTO dto, User author) {
        Blog blog = new Blog();
        blog.setBlogId(dto.getBlogId());
        blog.setAuthor(author); // author phải lấy từ DB theo authorId
        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        blog.setImageUrl(dto.getImageUrl());
        blog.setCreatedAt(dto.getCreatedAt());
        blog.setStatus(dto.getStatus() != null ? Blog.BlogStatus.valueOf(dto.getStatus()) : null);
        blog.setBlogType(dto.getBlogType() != null ? Blog.BlogType.valueOf(dto.getBlogType()) : null);
        return blog;
    }

    public Map<String, Long> getBlogCategoryCount() {
        List<Object[]> result = blogRepository.countBlogByType();
        Map<String, Long> map = new java.util.HashMap<>();
        for (Object[] row : result) {
            map.put(row[0] != null ? row[0].toString() : "OTHER", (Long) row[1]);
        }
        return map;
    }

    @Override
    public List<BlogDTO> getRelatedBlogs(Long blogId, int limit) {
        Blog current = blogRepository.findById(blogId)
            .orElseThrow(() -> new RuntimeException("Blog không tồn tại"));
        List<Blog> all = blogRepository.findAllWithAuthor();
        return all.stream()
            .filter(b -> !b.getBlogId().equals(blogId))
            .filter(b -> b.getStatus() != null && b.getStatus().name().equals("PUBLISHED"))
            .filter(b -> b.getBlogType() != null && b.getBlogType().equals(current.getBlogType()))
            .limit(limit)
            .map(this::toDTO)
            .toList();
    }
}