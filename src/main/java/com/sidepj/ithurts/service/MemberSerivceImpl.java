package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.repository.MemberRepository;
import com.sidepj.ithurts.service.dto.MemberControllerDTO;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberSerivceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<MemberControllerDTO> getMembers() {
        List<Member> allMembers = memberRepository.findAll();
        return getMemberControllerDTOs(allMembers);
    }

    @Override
    public MemberControllerDTO getMemberByName(String memberName) {
        Optional<Member> findMember = memberRepository.findMemberByName(memberName);
        return findMember.map(MemberControllerDTO::new).orElse(null);
    }

    @Override
    public MemberControllerDTO getMemberById(Long id) {
        if(memberRepository.findById(id).isPresent()){
            Optional<Member> fiindMember = memberRepository.findById(id);
            return new MemberControllerDTO(fiindMember.get());
        }
        else throw new IllegalArgumentException("존재하지 않는 회원입니다.");
    }

    @Override
    public MemberControllerDTO updateMemberById(Long id, MemberJoinDTO memberJoinDTO) {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()){
            Member member = findMember.get();
            if(memberJoinDTO.getUsername() != null && StringUtils.hasText(memberJoinDTO.getUsername())){
                member.setName(memberJoinDTO.getUsername());
            }

            if(memberJoinDTO.getPassword() != null && StringUtils.hasText(memberJoinDTO.getPassword())){
                member.setPassword(memberJoinDTO.getPassword());
                member.setLastPwdChanged(LocalDateTime.now());
            }
            return new MemberControllerDTO(member);
        }
        else{
            throw new IllegalArgumentException("정보를 업데이트 할 수 없습니다 관리자에게 문의하세요");
        }
    }

    @Override
    public void deleteAccount(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()){
            Member member = findMember.get();
            try {
                memberRepository.delete(member);
            } catch (Exception e){
                throw new IllegalArgumentException("해당 계정을 삭제할 수 없습니다 관리자에게 문의하세요.");
            }
        }
    }

    @Override
    public String getRole(String memberName) {
        Optional<Member> findMember = memberRepository.findMemberByName(memberName);
        if(findMember.isPresent()){
            Member member = findMember.get();
            return member.getRole();
        }
        else{
            return null;
        }
    }

    @Override
    public MemberControllerDTO join(MemberJoinDTO memberJoinDTO, String userRole) {
        checkMemberValidity(memberJoinDTO, userRole);
        Member savedMember = memberRepository.save(new Member(memberJoinDTO, userRole));
        return new MemberControllerDTO(savedMember);

    }

    private List<MemberControllerDTO> getMemberControllerDTOs(List<Member> allMembers) {
        List<MemberControllerDTO> allMembersTransferedDTO = new ArrayList<>();
        for (Member allMember : allMembers) {
            MemberControllerDTO memberControllerDTO = new MemberControllerDTO(allMember);
            allMembersTransferedDTO.add(memberControllerDTO);
        }
        return allMembersTransferedDTO;
    }


    // DTO for admin page
    private List<MemberJoinDTO> getMemberJoinDTOs(List<Member> allMembers) {
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


        Optional<Member> memberByName = memberRepository.findMemberByName(memberJoinDTO.getUsername());
        if(memberByName.isPresent()){
            throw new IllegalArgumentException("이미 동일한 이름의 계정이 존재합니다");
        }
    }
}
