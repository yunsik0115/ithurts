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
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member postMember; // fk for indicate which user created this post.

    @Column(name = "post_name")
    private String postName;

    @Column(name = "post_content")
    private String content;

    @Column(name = "post_created_date")
    private LocalDateTime createdDate;

    @Column(name = "post_modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "post_type")
    private String postType;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Love> loves = new ArrayList<>();

}
