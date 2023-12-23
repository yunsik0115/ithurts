package com.sidepj.ithurts.service;

import com.google.common.hash.Hashing;
import com.sidepj.ithurts.controller.dto.ErrorResult;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public void login(String username, String password, HttpServletRequest request, HttpServletResponse response) throws Exception{

        // TO-DO 패스워드 암/복호화
        // https://seed.kisa.or.kr/kisa/Board/38/detailView.do KISA 암/복호화 가이드라인 규칙을 따르고자 함.
        // 보안강도 112비트 이상 필요

        Optional<Member> findMemberOptional = memberRepository.findMemberByName(username);
        String encPwd = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();

        if(findMemberOptional.isPresent() && findMemberOptional.get().getPassword().equals(encPwd)){
            Member loginMember = findMemberOptional.get();
            HttpSession session = request.getSession();
            session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        } else {
            throw new AccessDeniedException("아이디 비밀번호를 확인하세요");
        }
    }

    public void logout(HttpServletRequest request){
        //sessionManager.expire(request);
        HttpSession session = request.getSession();
        session.invalidate();
    }


}
