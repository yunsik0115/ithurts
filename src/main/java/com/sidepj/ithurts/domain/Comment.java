package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    private int id;

    private int postId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User comment_user;

    private String content;

    private int likeCnt;
}
