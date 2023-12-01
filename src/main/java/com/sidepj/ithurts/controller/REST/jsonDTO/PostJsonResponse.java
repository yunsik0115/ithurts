package com.sidepj.ithurts.controller.REST.jsonDTO;

import com.sidepj.ithurts.controller.REST.jsonDTO.data.CommentJSON;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.LoveJSON;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.PostJSON;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PostJsonResponse {

    private int status;

    private String message;

    private PostJSON postJSON;

    private List<CommentJSON> commentJSON;


    public PostJsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public PostJsonResponse(int status, String message, PostJSON postJSON) {
        this.status = status;
        this.message = message;
        this.postJSON = postJSON;
    }

    public PostJsonResponse(int status, String message, PostJSON postJSON, List<CommentJSON> commentJSON) {
        this.status = status;
        this.message = message;
        this.postJSON = postJSON;
        this.commentJSON = commentJSON;
    }
}
