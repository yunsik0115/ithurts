package com.sidepj.ithurts.controller.REST.jsonDTO;

import com.sidepj.ithurts.controller.REST.jsonDTO.data.CommentJSON;
import com.sidepj.ithurts.domain.Comment;

import java.util.List;

public class CommentJsonResponse {

    private int status;

    private String message;

    private List<CommentJSON> comments;

    public CommentJsonResponse(int status, String message, List<CommentJSON> comments) {
        this.status = status;
        this.message = message;
        this.comments = comments;
    }

    public CommentJsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.comments = null;
    }
}
