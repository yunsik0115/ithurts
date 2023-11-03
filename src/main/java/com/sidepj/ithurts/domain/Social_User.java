package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Social_User {

    @Id
    private int id;

    @ManyToOne
    @JoinTable(name = "user_id")
    private User user;

    private String socialType; // Referenced From Naver API Specification

    private String socialProfile;

    private LocalDateTime connectedDate;

}
