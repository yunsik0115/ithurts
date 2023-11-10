package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.domain.Report;
import com.sidepj.ithurts.repository.MemberRepository;
import com.sidepj.ithurts.service.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberSerivceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<MemberDTO> getMembers() {
        List<Member> allMembers = memberRepository.findAll();
        return getMemberDTOS(allMembers);
    }

    @Override
    public MemberDTO getMember(String memberName) {
        return new MemberDTO(memberRepository.findMemberByName(memberName));
    }

    @Override
    public String getRole(String memberName) {
        return new MemberDTO(memberRepository.findMemberByName(memberName)).getRole();
    }

    @Override
    public Member join(MemberDTO memberDTO, String userRole) {
        checkMemberValidity(memberDTO, userRole);
        return new Member(memberDTO, userRole);
    }

    private List<MemberDTO> getMemberDTOS(List<Member> allMembers) {
        List<MemberDTO> allMembersTransferedDTO = new ArrayList<>();
        for (Member allMember : allMembers) {
            MemberDTO memberDTO = new MemberDTO(allMember);
            allMembersTransferedDTO.add(memberDTO);
        }
        return allMembersTransferedDTO;
    }

    private void checkMemberValidity(MemberDTO memberDTO, String userRole){
        if(memberDTO.getUsername() == null || !StringUtils.hasText(memberDTO.getUsername())){
            throw new IllegalArgumentException("이름을 입력해야 합니다");
        }
        if(memberDTO.getPassword() == null || !StringUtils.hasText(memberDTO.getPassword())){
            throw new IllegalArgumentException("비밀번호를 입력하세요");
        }
        if(!StringUtils.hasText(userRole)){
            throw new IllegalArgumentException("권한 정보를 획득할 수 없습니다, 관리자에게 문의하세요");
        }
    }
}
