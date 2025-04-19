package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<List<Comment>> findByPostId(Long id);

    Page<Comment> findByPostId(Long id, Pageable pageable);

    Optional<List<Comment>> findByAuthorId(Long id);

    Page<Comment> findByAuthorId(Long id, Pageable pageable);

    void removeCommentById(Long commentId);
}
