package com.sidepj.ithurts.domain;

import com.sidepj.ithurts.service.dto.PostDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Data
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id")
    private Long id;

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
