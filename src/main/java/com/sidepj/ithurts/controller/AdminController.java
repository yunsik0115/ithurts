//package com.sidepj.ithurts.controller;
//
//import com.sidepj.ithurts.domain.Hospital;
//import com.sidepj.ithurts.domain.Member;
//import com.sidepj.ithurts.domain.Pharmacy;
//import com.sidepj.ithurts.service.DataService;
//import com.sidepj.ithurts.service.MemberService;
//import com.sidepj.ithurts.service.PostService;
//import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
//import com.sidepj.ithurts.service.dto.MemberControllerDTO;
//import com.sidepj.ithurts.service.dto.MemberJoinDTO;
//import com.sidepj.ithurts.service.dto.PharmacyControllerDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequestMapping("/admin/manage")
//@RequiredArgsConstructor
//public class AdminController {
//
//    private final PostService postService;
//    private final MemberService memberService;
//    private final DataService<Hospital> hospitalService;
//    private final DataService<Pharmacy> pharmacyService;
//
//    // TO - DO 일관성 있는 naming 규칙 적용 필요 user <-> member 불일치 등등
//    // Admin의 경우 전체 접근 / 수정 가능해야하기 때문에 서비스 대신 Repository로 전체 정보 직접 접근
//
//    @GetMapping("/users")
//    public String getAllAccountInfo(Model model){
//        List<Member> members = memberService.getMembers();
//        model.addAttribute("accounts", getMemberControllerDTOs(members));
//        return "users.html";
//    }
//
//    @GetMapping("/users/{userId}")
//    public String getAccountInfo(@PathVariable Long userId, Model model){
//        Member findMember = memberService.getMemberById(userId);
//        model.addAttribute("user", getMemberControllerDTO(findMember));
//        return "user.html";
//    }
//
//    @PatchMapping("/users/{userId}")
//    public String updateAccountInfo(@PathVariable Long userId, @ModelAttribute MemberJoinDTO memberJoinDTO, Model model){
//        Member member = memberService.updateMemberById(userId, memberJoinDTO);
//        model.addAttribute("updatedUser", getMemberControllerDTO(member));
//        return "user.html";
//    }
//
//    // Repository hasn't implemented yet.
////    @GetMapping("/messages")
////    public String getAllNotifications(){
////        return "notification.html";
////    }
////
////    @GetMapping("/message/{messageId}")
////    public String getNotification(){
////        return "notification.html";
////    }
////
////    @GetMapping("/message")
////    public String notificationForm(){
////        return "notification.html";
////    }
////
////    @PostMapping("/message")
////    public String notification(){
////        return "notificiation.html";
////    }
//
//    @GetMapping("/reports")
//    public String reports(){
//        return "reports.html";
//    }
//
//    @GetMapping("/reports/{reportId}")
//    public String getReport(@PathVariable Long reportId, Model model){
//        //reportService.getReport(reportId);
//        return "report.html";
//    }
//
////    @PostMapping("/reports/{reportId}")
////    public String processReport(){
////
////        return "report.html";
////    }
//
//    @PatchMapping("/reports/{reportId}")
//    public String modifyingReport(@PathVariable Long reportId){
//        //reportService.findById(reportId);
//
//        return "report.html";
//    }
//
//    @DeleteMapping("/reports/{reportId}")
//    public String deleteReport(@PathVariable Long reportId){
//        //reportService.remove(reportId);
//        return "report.html";
//    }
//
//    @GetMapping("/metainfo/hospitals")
//    public String getAllHospitals(){
//        return "hospitals.html";
//    }
//
//    @GetMapping("/metainfo/hospitals/{hospitalId}")
//    public String getHospitalInfo(){
//        return "hospital.html";
//    }
//
//    @PatchMapping("/metainfo/hospitals/{hospitalId}")
//    public String updateHospitalInfo(){
//        // 병원 정보를 OPENAPI에서 받아올지 여부와
//        // 임의적으로 병원 정보를 변경할 수 있음.
//        return "hospital.html";
//    }
//
//    @DeleteMapping("/metainfo/hospitals/{hospitalId}")
//    public String removeHospitalInfo(){
//        return "hospital.html";
//    }
//
//    @PostMapping("/metainfo/hospitals/retrieve")
//    public String retrieveAllHospitals(){
//        // Warning - Very Time Consuming Task
//        return "hospital.html";
//    }
//
//    @GetMapping("/metainfo/pharmacies")
//    public String getAllPharmacies(){
//        return "pharmacies.html";
//    }
//
//
//    @GetMapping("/metainfo/pharmacies/{pharmacyId}")
//    public String getPharmacyInfo(){
//        return "pharmacy.html";
//    }
//
//    @PatchMapping("/metainfo/pharmacies/{pharmacyId}")
//    public String updatePharmacyInfo(){
//        // 병원 정보를 OPENAPI에서 받아올지 여부와
//        // 임의적으로 병원 정보를 변경할 수 있음.
//        return "pharmacy.html";
//    }
//
//    @DeleteMapping("/metainfo/pharmacies/{pharmacyId}")
//    public String removePharmacyInfo(){
//        return "pharmacy.html";
//    }
//
//    @PostMapping("/metainfo/pharmacies/retrieve")
//    public String retrieveAllPharmacies(){
//        // Warning - Very Time Consuming Task
//        return "pharmacy.html";
//    }
//
//    @GetMapping("/posts")
//    public String getPosts(){
//        return "posts.html";
//    }
//
//    @GetMapping("/post/{postId}")
//    public String getPost(){
//        return "post.html";
//    }
//
//    @GetMapping("/post")
//    public String postForm(){
//        return "postForm.html";
//    }
//
//    @PostMapping("/post")
//    public String addPost(){
//        // 공지기능을 사용할 수 있음
//        return "post.html";
//    }
//
//    @PatchMapping("/post/{postId}")
//    public String modifyPost(){
//        // block 처리 등 가능
//        return "post.html";
//    }
//
//    @DeleteMapping("/post/{postId}")
//    public String deletePost(){
//        // 관리자가 삭제한 경우, 관리자에 의해 삭제된 게시물입니다(로 표시)
//        // 관리자만 조회 가능, 외부 조회 불가.
//        return "post.html";
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
//
//
//
//}
