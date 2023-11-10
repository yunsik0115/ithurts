package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.domain.Report;
import com.sidepj.ithurts.service.dto.MemberDTO;

import java.util.List;

public interface MemberService {
    List<MemberDTO> getMembers();

    MemberDTO getMember(String memberName);

    String getRole(String memberName);

    Member join(MemberDTO member, String userRole);


}
