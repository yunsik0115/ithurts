package com.sidepj.ithurts.controller.REST.jsonDTO;

import com.sidepj.ithurts.controller.REST.jsonDTO.data.PostJSON;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostsJsonResponse {

    private int status;
    private String message;

    private List<PostJSON> posts;

    public PostsJsonResponse(int status, String message, List<PostJSON> posts) {
        this.status = status;
        this.message = message;
        this.posts = posts;
    }

    public PostsJsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.posts = null;
    }
}
