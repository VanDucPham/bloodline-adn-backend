package com.example.Bloodline_ADN_System.dto.noneWhere;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BlogDTO {
    private Long blogId;
    private Long authorId; // chỉ lấy id hoặc có thể lấy tên tác giả nếu muốn
    private String authorName; // tên tác giả
    private String title;

    private String content;

    private String imageUrl;

    private String status; // DRAFT, PUBLISHED, ARCHIVED
    private String blogType; // GUIDE, NEWS, OTHER, POLICY, PROMOTION
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}