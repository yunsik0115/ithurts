package com.sidepj.ithurts.service.dto;

import com.sidepj.ithurts.domain.Member;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDTO {

    private Long id;

    private String username;

    private String password;

    private String role;

    private LocalDateTime lastPwdChanged;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.username = member.getName();
        this.password = member.getPassword();
        this.role = member.getRole();
        this.lastPwdChanged = member.getLastPwdChanged();
    }
}
