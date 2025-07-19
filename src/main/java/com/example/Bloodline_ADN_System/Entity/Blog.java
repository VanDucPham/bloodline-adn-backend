package com.example.Bloodline_ADN_System.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

// ========================
// 📝 BLOG ENTITY
// ========================
@Entity
@Table(name = "blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishDate;

    @Enumerated(EnumType.STRING)
    private BlogStatus status;

    @Enumerated(EnumType.STRING)
    private BlogType blogType;

    public BlogType getBlogType() {
        return blogType;
    }
    public void setBlogType(BlogType blogType) {
        this.blogType = blogType;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum BlogStatus { DRAFT, PUBLISHED, ARCHIVED }
    public enum BlogType { NEWS, GUIDE, POLICY, PROMOTION, OTHER }
}
