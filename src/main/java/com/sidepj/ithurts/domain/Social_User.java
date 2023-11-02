package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Social_User {

    @Id
    private int id;

    private int userId; // fk from user

    private String socialType; // Referenced From Naver API Specification

    private String socialProfile;

    private LocalDateTime connectedDate;

}
