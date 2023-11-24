package com.sidepj.ithurts.service;

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
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public void login(String username, String password, HttpServletRequest request, HttpServletResponse response) throws Exception{
        Optional<Member> findMemberOptional = memberRepository.findMemberByName(username);
        if(findMemberOptional.isPresent() && findMemberOptional.get().getPassword().equals(password)){
            // TO-DO 패스워드 암/복호화
            //sessionManager.createSession(findMember, response);
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
