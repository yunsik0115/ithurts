package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
