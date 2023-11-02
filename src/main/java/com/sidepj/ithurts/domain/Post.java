package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    private int id;

    private int userId; // fk for indicate which user created this post.

    private String postname;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String postType;

    private int commentCnt;

    private int likeCnt;

}
