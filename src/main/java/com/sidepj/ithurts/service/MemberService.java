package com.sidepj.ithurts.service;

import com.sidepj.ithurts.service.dto.MemberControllerDTO;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;

import java.util.List;

public interface MemberService {
    List<MemberControllerDTO> getMembers();

    MemberControllerDTO getMemberByName(String memberName);

    MemberControllerDTO getMemberById(Long id);

    MemberControllerDTO updateMemberById(Long id, MemberJoinDTO memberJoinDTO);

    String getRole(String memberName);

    MemberControllerDTO join(MemberJoinDTO member, String userRole);

    void deleteAccount(Long id);


}
