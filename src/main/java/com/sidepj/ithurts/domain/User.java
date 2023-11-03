package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    private int id;

    @OneToMany(mappedBy = "user")
    private List<Social_User> socialUser = new ArrayList<>();

    private String username;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime lastPwdChanged;

    private String role;

    boolean isPwdExpired;

    boolean isAccountLocked;

    boolean isLocked;


}
