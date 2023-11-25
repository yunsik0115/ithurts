package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.controller.dto.ErrorResult;
import com.sidepj.ithurts.controller.dto.LoginForm;
import com.sidepj.ithurts.service.LoginService;
import com.sidepj.ithurts.service.MemberService;
import com.sidepj.ithurts.service.dto.MemberControllerDTO;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
    public ResponseEntity<MemberControllerDTO> signUp(@RequestBody MemberJoinDTO memberJoinDTO){
        MemberControllerDTO join = memberService.join(memberJoinDTO, "User");
        return new ResponseEntity<MemberControllerDTO>(join, HttpStatus.ACCEPTED);
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
}
