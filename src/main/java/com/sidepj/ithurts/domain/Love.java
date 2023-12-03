package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "loves") @Data
public class Love {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "love_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member love_member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

}
