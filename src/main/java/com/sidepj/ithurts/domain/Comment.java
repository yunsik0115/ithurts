package com.sidepj.ithurts.domain;

import com.sidepj.ithurts.service.dto.PostDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
public class Comment {
    @Id
    @Column(name = "comment_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member author;

    @JoinColumn(name = "comment_content")
    private String content;

    @OneToMany(mappedBy = "comment")
    private List<Love> loves =new ArrayList<>();

}
