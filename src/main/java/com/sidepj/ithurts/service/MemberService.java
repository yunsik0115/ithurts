package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;

import java.util.List;

public interface MemberService {
    List<MemberJoinDTO> getMembers();

    MemberJoinDTO getMember(String memberName);

    String getRole(String memberName);

    Member join(MemberJoinDTO member, String userRole);


}
