package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member {

    @Id
    @Column(name = "member_id")
    private int id;

    @OneToMany(mappedBy = "report_member")
    private List<Report> reps = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<SocialMember> socialMembers = new ArrayList<>();

    @OneToMany(mappedBy = "comment_member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post_member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "love_member")
    private List<Love> loves = new ArrayList<>();

    private String username;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime lastPwdChanged;

    private String role;

    boolean isPwdExpired;

    boolean isAccountLocked;

    boolean isLocked;


}
