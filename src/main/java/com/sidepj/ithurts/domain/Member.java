package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "member_id")
    private int id;

    @OneToMany(mappedBy = "report_member")
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<SocialMember> socialUser = new ArrayList<>();

    @OneToMany(mappedBy = "comment_member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post_member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "like_member")
    private List<Like> likes = new ArrayList<>();

    private String username;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime lastPwdChanged;

    private String role;

    boolean isPwdExpired;

    boolean isAccountLocked;

    boolean isLocked;


}
