package com.sidepj.ithurts.service;

import com.google.common.hash.Hashing;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.repository.MemberRepository;
import com.sidepj.ithurts.service.dto.MemberControllerDTO;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberSerivceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<Member> getMembers() {
        List<Member> allMembers = memberRepository.findAll();
        return allMembers;
    }

    @Override
    public Member getMemberByName(String memberName) {
        Optional<Member> findMember = memberRepository.findMemberByName(memberName);
        if(findMember.isPresent()){
            return findMember.get();
        }
        else{
            throw new IllegalArgumentException("해당 이름을 포함한 회원을 찾을 수 없습니다");
        }
    }

    @Override
    public Member getMemberById(Long id) {

        Optional<Member> findMember = memberRepository.findById(id);
        if(findMember.isPresent()) {
            return findMember.get();
        }
        else throw new IllegalArgumentException("존재하지 않는 회원입니다.");
    }

    @Override
    public Member updateMemberById(Long id, MemberJoinDTO memberJoinDTO) {

        if(memberJoinDTO.getUsername().isBlank() && memberJoinDTO.getPassword().isBlank()){
            throw new IllegalArgumentException("두 필드 중 하나는 업데이트해야 합니다");
        }


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
            return member;
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
    public Member join(MemberJoinDTO memberJoinDTO, String userRole) {
        String encPwd = Hashing.sha256().hashString(memberJoinDTO.getPassword(), StandardCharsets.UTF_8).toString();
        log.info("encrypted password = {}", encPwd);
        memberJoinDTO.setPassword(encPwd);
        checkMemberValidity(memberJoinDTO, userRole);
        return memberRepository.save(new Member(memberJoinDTO, userRole));
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

    @Override
    public Page<Member> getMembers(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();


        Pageable pageRequest = PageRequest.of(page, size);
        return memberRepository.findAll(pageRequest);
    }

    @Override
    public Page<Member> getMemberByNameLike(String memberName, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        Pageable pageRequest = PageRequest.of(page, size);

        return memberRepository.findMemberByNameLike(memberName, pageRequest);
    }
}
