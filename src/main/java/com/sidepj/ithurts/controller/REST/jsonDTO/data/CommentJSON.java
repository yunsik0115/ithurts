package com.sidepj.ithurts.controller.REST.jsonDTO.data;

import lombok.Data;

@Data
public class CommentJSON {

    private Long id;

    private String content;

    private Long postId;

    private String author;

    private Integer lovesCount;

}
