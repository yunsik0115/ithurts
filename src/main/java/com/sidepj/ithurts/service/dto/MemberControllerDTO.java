package com.sidepj.ithurts.service.dto;

import com.sidepj.ithurts.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MemberControllerDTO {

    private Long id;

    private String username;

    private String role;


    public MemberControllerDTO(Member member) {
        this.id = member.getId();
        this.username = member.getName();
        this.role = member.getRole();
    }

}
