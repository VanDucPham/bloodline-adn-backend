package com.example.Bloodline_ADN_System.service;

import com.example.Bloodline_ADN_System.dto.noneWhere.BlogDTO;
//import com.example.Bloodline_ADN_System.Entity.User;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BlogService {
    List<BlogDTO> getAllBlogDTO();
    Optional<BlogDTO> getBlogById(Long id);
    BlogDTO createBlog(BlogDTO dto);
    BlogDTO updateBlog(Long id, BlogDTO dto);
    void deleteBlog(Long id);
    Page<BlogDTO> getBlogsPage(Pageable pageable, String status, String type, Long authorId);
    java.util.Map<String, Long> getBlogCategoryCount();
    List<BlogDTO> getRelatedBlogs(Long blogId, int limit);
}