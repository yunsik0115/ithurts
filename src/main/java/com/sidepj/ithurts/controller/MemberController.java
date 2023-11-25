package com.sidepj.ithurts.controller;

import com.sidepj.ithurts.service.MemberService;
import com.sidepj.ithurts.service.dto.MemberControllerDTO;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/logout/{memberId}")
    public String logout(){
        return "/";
    }

    @PostMapping("/logout/{memberId}/OAuth")
    public String logoutOAuth(){
        return "/";
    }

    @GetMapping("/{memberId}")
    public String getAccountInfo(@PathVariable Long memberId, Model model){
        MemberControllerDTO member = memberService.getMemberById(memberId);
        model.addAttribute("member", member);
        return "myPage.html";
    }

    @PatchMapping("/{memberId}")
    public String modifyAccountInfo(@PathVariable Long memberId, @ModelAttribute MemberJoinDTO memberJoinDTO, Model model){
        memberService.updateMemberById(memberId, memberJoinDTO);
        return "myPage.html";
    }

    @DeleteMapping("/{memberId}")
    public String removeAccount(@PathVariable Long memberId){
        memberService.deleteAccount(memberId);
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
