package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Posts")
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

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Love> loves = new ArrayList<>();

}
