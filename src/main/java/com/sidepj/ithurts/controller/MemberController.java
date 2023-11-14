package com.sidepj.ithurts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
public class MemberController {

    @PostMapping("/logout/{memberId}")
    public String logout(){
        return "/";
    }

    @PostMapping("/logout/{memberId}/OAuth")
    public String logoutOAuth(){
        return "/";
    }

    @GetMapping("/{memberId}")
    public String getAccountInfo(){
        return "myPage.html";
    }

    @PatchMapping("/{memberId}")
    public String modifyAccountInfo(){
        return "myPage.html";
    }

    @DeleteMapping("/{memberId}")
    public String removeAccount(){
        return "/";
    }

    @DeleteMapping("/{memberId}/OAuth")
    public String removeOAuthAccount(){
        return "/";
    }

    @GetMapping
    public String getNotificiations(){
        return "ASYNC"; // Will be done after SYNC controllers implemented.
    }
}
