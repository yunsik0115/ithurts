package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member post_member; // fk for indicate which user created this post.

    private String postname;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String postType;

    private int commentCnt;

    private int likeCnt;

}
