package com.board.board.repository;

import com.board.board.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String title);
    List<Post> findByAuthorContaining(String author);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<Post> findByTitleOrContentContaining(@Param("keyword") String keyword);

    Page<Post> findAllByOrderByViewCountDesc(Pageable pageable);
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}