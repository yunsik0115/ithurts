package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long id);

    List<Comment> findByAuthorId(Long id);

    void removeCommentById(Long commentId);
}
