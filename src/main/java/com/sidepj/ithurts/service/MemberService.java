package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.service.dto.MemberControllerDTO;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    List<Member> getMembers();
    Page<Member> getMembers(Pageable pageable);

    Member getMemberByName(String memberName);

    Page<Member> getMemberByNameLike(String memberName, Pageable pageable);

    Member getMemberById(Long id);

    Member updateMemberById(Long id, MemberJoinDTO memberJoinDTO);

    String getRole(String memberName);

    Member join(MemberJoinDTO member, String userRole);

    void deleteAccount(Long id);


}
