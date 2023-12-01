package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.service.dto.MemberControllerDTO;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;

import java.util.List;

public interface MemberService {
    List<Member> getMembers();

    Member getMemberByName(String memberName);

    Member getMemberById(Long id);

    Member updateMemberById(Long id, MemberJoinDTO memberJoinDTO);

    String getRole(String memberName);

    Member join(MemberJoinDTO member, String userRole);

    void deleteAccount(Long id);


}
