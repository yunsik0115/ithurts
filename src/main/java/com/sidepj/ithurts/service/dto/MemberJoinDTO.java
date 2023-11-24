package com.sidepj.ithurts.service.dto;

import com.sidepj.ithurts.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MemberJoinDTO {

    private Long id;

    private String username;

    private String password;

    private String role;

    private LocalDateTime lastPwdChanged;

    public MemberJoinDTO(Member member) {
        this.id = member.getId();
        this.username = member.getName();
        //this.password = member.getPassword();
        this.role = member.getRole();
        this.lastPwdChanged = member.getLastPwdChanged();
    }

    public MemberJoinDTO(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
