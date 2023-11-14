package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Comment;

import java.util.List;

public interface CommentService {

    Comment saveComment(Comment comment);

    List<Comment> getCommentsByPost(Long postId);

    List<Comment> getCommentsByUser(Long userId);

    void deleteComment(Long commentId);



}
