package com.sidepj.ithurts.domain;

import com.sidepj.ithurts.service.dto.MemberDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "member_name")
    private String name;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_created_date")
    private LocalDateTime createdDate;

    @Column(name = "member_lastpassword_changed_date")
    private LocalDateTime lastPwdChanged;

    @Column(name = "member_role")
    private String role;

    @OneToMany(mappedBy = "report_member")
    private List<Report> reps = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<SocialMember> socialMembers = new ArrayList<>();

    @OneToMany(mappedBy = "comment_member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "postMember")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "love_member")
    private List<Love> loves = new ArrayList<>();


    public Member(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Member(MemberDTO memberDTO, String userRole){
        this.name = memberDTO.getUsername();
        this.password = memberDTO.getPassword();
        this.createdDate = LocalDateTime.now();
        this.role = userRole;
    }
}
