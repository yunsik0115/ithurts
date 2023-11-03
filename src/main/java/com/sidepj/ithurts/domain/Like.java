package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "my_like")
public class Like {
    @Id
    @Column(name = "like_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member like_member;

    private int postId;

    private int commentId;
}
