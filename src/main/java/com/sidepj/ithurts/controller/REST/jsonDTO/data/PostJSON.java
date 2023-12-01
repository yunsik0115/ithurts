package com.sidepj.ithurts.controller.REST.jsonDTO.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostJSON {

    // Post List를 보는 경우 Comments들까지 가져올 필요는 없음.
    // Like Count와 Love Count만 가져오고 나머지는 모두 Post에서 가져오면 됨

    private Long id;

    private String postName;

    private String content;

    private String author;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String postType;

    private Integer commentsCount;
    private Integer lovesCount;

}
