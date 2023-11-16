package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.service.dto.MemberControllerDTO;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;

import java.util.List;

public interface MemberService {
    List<MemberControllerDTO> getMembers();

    MemberControllerDTO getMember(String memberName);

    String getRole(String memberName);

    MemberControllerDTO join(MemberJoinDTO member, String userRole);


}
