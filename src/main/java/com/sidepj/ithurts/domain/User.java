package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    private int id;

    private String username;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime lastPwdChanged;

    private String role;

    boolean isPwdExpired;

    boolean isAccountLocked;

    boolean isLocked;


}
