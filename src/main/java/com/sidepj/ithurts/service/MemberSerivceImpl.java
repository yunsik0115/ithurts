package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.repository.MemberRepository;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberSerivceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<MemberJoinDTO> getMembers() {
        List<Member> allMembers = memberRepository.findAll();
        return getMemberDTOS(allMembers);
    }

    @Override
    public MemberJoinDTO getMember(String memberName) {
        return new MemberJoinDTO(memberRepository.findMemberByName(memberName));
    }

    @Override
    public String getRole(String memberName) {
        return new MemberJoinDTO(memberRepository.findMemberByName(memberName)).getRole();
    }

    @Override
    public Member join(MemberJoinDTO memberJoinDTO, String userRole) {
        checkMemberValidity(memberJoinDTO, userRole);
        return memberRepository.save(new Member(memberJoinDTO, userRole));
    }

    private List<MemberJoinDTO> getMemberDTOS(List<Member> allMembers) {
        List<MemberJoinDTO> allMembersTransferedDTO = new ArrayList<>();
        for (Member allMember : allMembers) {
            MemberJoinDTO memberJoinDTO = new MemberJoinDTO(allMember);
            allMembersTransferedDTO.add(memberJoinDTO);
        }
        return allMembersTransferedDTO;
    }

    private void checkMemberValidity(MemberJoinDTO memberJoinDTO, String userRole){
        if(memberJoinDTO.getUsername() == null || !StringUtils.hasText(memberJoinDTO.getUsername())){
            throw new IllegalArgumentException("이름을 입력해야 합니다");
        }
        if(memberJoinDTO.getPassword() == null || !StringUtils.hasText(memberJoinDTO.getPassword())){
            throw new IllegalArgumentException("비밀번호를 입력하세요");
        }
        if(!StringUtils.hasText(userRole)){
            throw new IllegalArgumentException("권한 정보를 획득할 수 없습니다, 관리자에게 문의하세요");
        }


        Member memberByName = memberRepository.findMemberByName(memberJoinDTO.getUsername());
        if(memberByName != null){
            throw new IllegalArgumentException("이미 동일한 이름의 계정이 존재합니다");
        }
    }
}
