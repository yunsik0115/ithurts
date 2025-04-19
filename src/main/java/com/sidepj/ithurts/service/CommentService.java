package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CommentService {

    Comment saveComment(Long postId, Long memberId, Comment comment);

    List<Comment> getCommentsByPost(Long postId);

    Page<Comment> getCommentsByPost(Long postId, Pageable pageable);

    List<Comment> getCommentsByUser(Long userId);

    Page<Comment> getCommentsByUser(Long userId, Pageable pageable);

    Comment getCommentById(Long commentId) throws Exception;

    void deleteComment(Long commentId, Long memberId) throws Exception;



}
