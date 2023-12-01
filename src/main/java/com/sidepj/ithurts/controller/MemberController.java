//package com.sidepj.ithurts.controller;
//
//import com.sidepj.ithurts.domain.Member;
//import com.sidepj.ithurts.service.MemberService;
//import com.sidepj.ithurts.service.dto.MemberControllerDTO;
//import com.sidepj.ithurts.service.dto.MemberJoinDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequestMapping("/members")
//@RequiredArgsConstructor
//public class MemberController {
//
//    private final MemberService memberService;
//
//    @PostMapping("/logout/{memberId}")
//    public String logout(){
//        return "/";
//    }
//
//    @PostMapping("/logout/{memberId}/OAuth")
//    public String logoutOAuth(){
//        return "/";
//    }
//
//    @GetMapping("/{memberId}")
//    public String getAccountInfo(@PathVariable Long memberId, Model model){
//        Member memberById = memberService.getMemberById(memberId);
//        model.addAttribute("member", getMemberControllerDTO(memberById));
//        return "myPage.html";
//    }
//
//    @PatchMapping("/{memberId}")
//    public String modifyAccountInfo(@PathVariable Long memberId, @ModelAttribute MemberJoinDTO memberJoinDTO, Model model){
//        memberService.updateMemberById(memberId, memberJoinDTO);
//        return "myPage.html";
//    }
//
//    @DeleteMapping("/{memberId}")
//    public String removeAccount(@PathVariable Long memberId){
//        memberService.deleteAccount(memberId);
//        return "/";
//    }
//
//    @DeleteMapping("/{memberId}/OAuth")
//    public String removeOAuthAccount(){
//        return "/";
//    }
//
//    @GetMapping
//    public String getNotificiations(){
//        return "ASYNC"; // Will be done after SYNC controllers implemented.
//    }
//
//    private List<MemberControllerDTO> getMemberControllerDTOs(List<Member> allMembers) {
//        List<MemberControllerDTO> allMembersTransferedDTO = new ArrayList<>();
//        for (Member member : allMembers) {
//            allMembersTransferedDTO.add(getMemberControllerDTO(member));
//        }
//        return allMembersTransferedDTO;
//    }
//
//    private MemberControllerDTO getMemberControllerDTO(Member member){
//        return new MemberControllerDTO(member);
//    }
//
//
//    // DTO for admin page
//    private List<MemberJoinDTO> getMemberJoinDTOs(List<Member> allMembers) {
//        List<MemberJoinDTO> allMembersTransferedDTO = new ArrayList<>();
//        for (Member allMember : allMembers) {
//            MemberJoinDTO memberJoinDTO = new MemberJoinDTO(allMember);
//            allMembersTransferedDTO.add(memberJoinDTO);
//        }
//        return allMembersTransferedDTO;
//    }
//}
