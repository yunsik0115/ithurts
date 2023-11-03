package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "social_members")
public class SocialMember {

    @Id
    @Column(name = "social_id")
    private int id;

    @ManyToOne
    @JoinTable(name = "member_id") // Note that FK Column name cannot be declared arbitrarily.
    private Member member;

    @Column(name = "social_type")
    private String socialType; // Referenced From Naver API Specification

    @Column(name = "social_profile")
    private String socialProfile;

    @Column(name = "social_connected_date")
    private LocalDateTime connectedDate;

}
