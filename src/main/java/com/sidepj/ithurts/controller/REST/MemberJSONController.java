package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.service.LoginService;
import com.sidepj.ithurts.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json/members")
@RequiredArgsConstructor
public class MemberJSONController {

    private final MemberService memberService;
    private final LoginService loginService;
}
