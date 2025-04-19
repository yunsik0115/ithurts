package com.sidepj.ithurts.domain;

import com.sidepj.ithurts.service.dto.PostDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Posts")
@Getter @Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne // 추후 수정 (컨트롤러에서 유저 찾아서 넣어줘야함)
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


    // Constructor for testing (Temporary)
    public Post(String postName, String content) {
        this.postName = postName;
        this.content = content;
    }

    public Post(PostDTO postDTO){
        this.postName = postDTO.getName();
        this.content = postDTO.getContent();
        this.createdDate = LocalDateTime.now();
        this.postType = postDTO.getPostType();
    }

}
