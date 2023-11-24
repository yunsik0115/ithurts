package com.sidepj.ithurts.service;

import com.sidepj.ithurts.controller.dto.ErrorResult;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.repository.MemberRepository;
import com.sidepj.ithurts.service.Session.SessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    public void login(String username, String password, HttpServletResponse response){
        Optional<Member> findMember = memberRepository.findMemberByName(username);
        if(findMember.isPresent() && findMember.get().getPassword().equals(password)){
            // TO-DO 패스워드 암/복호화
            sessionManager.createSession(findMember, response);
        } else {
            throw new NoSuchElementException("아이디 비밀번호를 확인하세요");
        }
    }

    public void logout(HttpServletRequest request){
        sessionManager.expire(request);
    }

    @ExceptionHandler(IllegalAccessError.class)
    public ErrorResult loginHandleException(Exception e){
        return new ErrorResult("EX", "로그인 오류");
    }


}
