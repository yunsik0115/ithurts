package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.controller.REST.jsonDTO.MemberJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.StatusCode;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.UserJSON;
import com.sidepj.ithurts.controller.dto.LoginForm;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.service.LoginService;
import com.sidepj.ithurts.service.MemberService;
import com.sidepj.ithurts.service.SessionConst;
import com.sidepj.ithurts.service.dto.MemberControllerDTO;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/json/login")
@Slf4j
public class LoginJSONController {

    private final LoginService loginService;
    private final MemberService memberService;

    @PostMapping("/")
    @ResponseBody
    public String loginJson(@Valid @RequestBody LoginForm form, HttpServletRequest request ,HttpServletResponse response) throws Exception{
        isAlreadyLogin(request);
        loginService.login(form.getUsername(), form.getPassword(), request, response);
        return "ok";
    }


    @PostMapping("/logout")
    @ResponseBody
    public String logoutJson(HttpServletRequest request){
        loginService.logout(request);
        return "OK";
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity<MemberJsonResponse> signUp(@RequestBody MemberJoinDTO memberJoinDTO, HttpServletRequest request) throws Exception{
        isAlreadyLogin(request);
        Member user = memberService.join(memberJoinDTO, "User");
        UserJSON userJSON = userJSONFromEntity(user);
        return new ResponseEntity<MemberJsonResponse>(new MemberJsonResponse(StatusCode.OK, "성공 : 로그인 완료", userJSON), HttpStatus.ACCEPTED);
    }

    private static void isAlreadyLogin(HttpServletRequest request) throws AccessDeniedException {
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) != null){
            throw new AccessDeniedException("이미 로그인 되어있는 회원입니다.");
        }
    }

    /*
    === API Exception Process ===

    @ExceptionHandler
    // ResponseEntity<ErrorResult> 사용
    public ResponseEntity<ErrorResult> userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    // 상태 코드 지정
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    // 그냥 exception 사용
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

     */



    private UserJSON userJSONFromEntity(Member member){
        UserJSON userJSON = new UserJSON();
        userJSON.setId(member.getId());
        userJSON.setName(member.getName());
        userJSON.setCreatedDate(member.getCreatedDate());
        if(member.getLastPwdChanged() != null){
            userJSON.setLastPwdChanged(member.getLastPwdChanged());
        }
        userJSON.setRole(member.getRole());
        return userJSON;
    }

    private List<UserJSON> userJSONListFromUserJSON(List<Member> members){
        List<UserJSON> userJSONList = new ArrayList<>();
        for (Member member : members) {
            userJSONList.add(userJSONFromEntity(member));
        }
        return userJSONList;
    }
}
