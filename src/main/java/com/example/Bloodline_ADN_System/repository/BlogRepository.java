package com.example.Bloodline_ADN_System.repository;

import com.example.Bloodline_ADN_System.Entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query("SELECT b FROM Blog b JOIN FETCH b.author")
    List<Blog> findAllWithAuthor();

    @Query("SELECT b.blogType, COUNT(b) FROM Blog b WHERE b.status = 'PUBLISHED' GROUP BY b.blogType")
    List<Object[]> countBlogByType();

    @Query("SELECT b FROM Blog b JOIN FETCH b.author WHERE b.blogId = :id")
    Optional<Blog> findByIdWithAuthor(@Param("id") Long id);
}
