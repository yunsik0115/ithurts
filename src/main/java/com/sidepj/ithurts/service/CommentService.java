package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Comment;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface CommentService {

    Comment saveComment(Long postId, Long memberId, Comment comment);

    List<Comment> getCommentsByPost(Long postId);

    List<Comment> getCommentsByUser(Long userId);

    Comment getCommentById(Long commentId) throws Exception;

    void deleteComment(Long commentId, Long memberId) throws Exception;



}
