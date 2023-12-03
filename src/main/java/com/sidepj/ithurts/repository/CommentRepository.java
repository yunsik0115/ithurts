package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<List<Comment>> findByPostId(Long id);

    Optional<List<Comment>> findByAuthorId(Long id);

    void removeCommentById(Long commentId);
}
