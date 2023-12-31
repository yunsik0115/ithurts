package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.controller.REST.jsonDTO.MemberJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.MembersJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.StatusCode;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.UserJSON;
import com.sidepj.ithurts.controller.dto.ReportDTO;
import com.sidepj.ithurts.controller.dto.ReportDTOForAdmin;
import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.domain.Report;
import com.sidepj.ithurts.service.DataService;
import com.sidepj.ithurts.service.MemberService;
import com.sidepj.ithurts.service.PostService;
import com.sidepj.ithurts.service.ReportService;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/json/admin")
@RequiredArgsConstructor
public class AdminJSONController {

    private final PostService postService;
    private final MemberService memberService;
    private final ReportService reportService;
    private final DataService<Hospital> hospitalService;
    private final DataService<Pharmacy> pharmacyService;

    // TO - DO 일관성 있는 naming 규칙 적용 필요 user <-> member 불일치 등등

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public ResponseEntity<MembersJsonResponse> getAllAccountInfo(){
        List<Member> members = memberService.getMembers();
        MembersJsonResponse membersJsonResponse = new MembersJsonResponse(StatusCode.OK, "성공 : 전체 멤버 조회하기", userJSONListFromUserJSON(members));
        return new ResponseEntity<>(membersJsonResponse, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{userId}")
    public ResponseEntity<MemberJsonResponse> getAccountInfo(@PathVariable Long userId){
        Member findMember = memberService.getMemberById(userId);
        MemberJsonResponse memberJsonResponse = new MemberJsonResponse(StatusCode.OK, "성공 : 단건 멤버 조회하기", userJSONFromEntity(findMember));
        return new ResponseEntity<>(memberJsonResponse, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/users/{userId}")
    @ResponseBody
    public ResponseEntity<MemberJsonResponse> updateAccountInfo(@PathVariable Long userId, @RequestBody MemberJoinDTO memberJoinDTO){
        Member updatedMemberById = memberService.updateMemberById(userId, memberJoinDTO);
        MemberJsonResponse memberJsonResponse = new MemberJsonResponse(StatusCode.OK, "성공 : 해당 회원 업데이트 성공", userJSONFromEntity(updatedMemberById));
        return new ResponseEntity<>(memberJsonResponse,HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/user/add")
    @ResponseBody
    public ResponseEntity<MemberJsonResponse> addAccount(@RequestBody MemberJoinDTO memberJoinDTO){
        Member user = memberService.join(memberJoinDTO, "User");
        MemberJsonResponse memberJsonResponse = new MemberJsonResponse(StatusCode.OK, "성공 : 회원 정보를 기반으로 유저 가입", userJSONFromEntity(user));
        return new ResponseEntity<>(memberJsonResponse, HttpStatus.OK);
    }

    // Repository hasn't implemented yet.
//    @GetMapping("/messages")
//    public String getAllNotifications(){
//        return "notification.html";
//    }
//
//    @GetMapping("/message/{messageId}")
//    public String getNotification(){
//        return "notification.html";
//    }
//
//    @GetMapping("/message")
//    public String notificationForm(){
//        return "notification.html";
//    }
//
//    @PostMapping("/message")
//    public String notification(){
//        return "notificiation.html";
//    }

    @GetMapping("/reports")
    public ResponseEntity<List<ReportDTOForAdmin>> reports(){
        List<Report> reports = reportService.getReports();
        List<ReportDTOForAdmin> reportDTOS = dtoListFromEntities(reports);
        return new ResponseEntity<>(reportDTOS, HttpStatus.OK);
    }

    @GetMapping("/reports/{reportId}")
    public ResponseEntity<ReportDTOForAdmin> getReport(@PathVariable Long reportId){
        Report report = reportService.getReport(reportId);
        ReportDTOForAdmin reportDTOForAdmin = getReportDTOForAdmin(reportId);
        return new ResponseEntity<>(reportDTOForAdmin, HttpStatus.OK);
    }

//    @PostMapping("/reports/{reportId}")
//    public String processReport(){
//
//        return "report.html";
//    }

    @PatchMapping("/reports/{reportId}")
    public ResponseEntity<ReportDTOForAdmin> modifyingReport(@PathVariable Long reportId, @RequestParam Boolean status) throws IllegalAccessException {

        Report report = reportService.checkStatus(reportId, status);

        ReportDTOForAdmin reportDTO = reportDTOFromEntity(report);

        return new ResponseEntity<>(reportDTO, HttpStatus.OK);
    }



    @DeleteMapping("/reports/{reportId}")
    public String deleteReport(@PathVariable Long reportId){
        //reportService.remove(reportId);
        return "report.html";
    }

    @GetMapping("/metainfo/hospitals")
    public String getAllHospitals(){
        return "hospitals.html";
    }

    @GetMapping("/metainfo/hospitals/{hospitalId}")
    public String getHospitalInfo(){
        return "hospital.html";
    }

    @PatchMapping("/metainfo/hospitals/{hospitalId}")
    public String updateHospitalInfo(){
        // 병원 정보를 OPENAPI에서 받아올지 여부와
        // 임의적으로 병원 정보를 변경할 수 있음.
        return "hospital.html";
    }

    @DeleteMapping("/metainfo/hospitals/{hospitalId}")
    public String removeHospitalInfo(){
        return "hospital.html";
    }

    @PostMapping("/metainfo/hospitals/retrieve")
    public String retrieveAllHospitals(){
        // Warning - Very Time Consuming Task
        return "hospital.html";
    }

    @GetMapping("/metainfo/pharmacies")
    public String getAllPharmacies(){
        return "pharmacies.html";
    }


    @GetMapping("/metainfo/pharmacies/{pharmacyId}")
    public String getPharmacyInfo(){
        return "pharmacy.html";
    }

    @PatchMapping("/metainfo/pharmacies/{pharmacyId}")
    public String updatePharmacyInfo(){
        // 병원 정보를 OPENAPI에서 받아올지 여부와
        // 임의적으로 병원 정보를 변경할 수 있음.
        return "pharmacy.html";
    }

    @DeleteMapping("/metainfo/pharmacies/{pharmacyId}")
    public String removePharmacyInfo(){
        return "pharmacy.html";
    }

    @PostMapping("/metainfo/pharmacies/retrieve")
    public String retrieveAllPharmacies(){
        // Warning - Very Time Consuming Task
        return "pharmacy.html";
    }

    @GetMapping("/posts")
    public String getPosts(){
        return "posts.html";
    }

    @GetMapping("/post/{postId}")
    public String getPost(){
        return "post.html";
    }

    @GetMapping("/post")
    public String postForm(){
        return "postForm.html";
    }

    @PostMapping("/post")
    public String addPost(){
        // 공지기능을 사용할 수 있음
        return "post.html";
    }

    @PatchMapping("/post/{postId}")
    public String modifyPost(){
        // block 처리 등 가능
        return "post.html";
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(){
        // 관리자가 삭제한 경우, 관리자에 의해 삭제된 게시물입니다(로 표시)
        // 관리자만 조회 가능, 외부 조회 불가.
        return "post.html";
    }

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

    private List<ReportDTOForAdmin> dtoListFromEntities(List<Report> reports){
        List<ReportDTOForAdmin> list = new ArrayList<>();
        for (Report report : reports) {
            ReportDTOForAdmin reportDTO = reportDTOFromEntity(report);
            list.add(reportDTO);
        }
        return list;
    }

    private ReportDTOForAdmin reportDTOFromEntity(Report report){
        ReportDTOForAdmin reportDTO = new ReportDTOForAdmin();
        reportDTO.setId(report.getId());
        reportDTO.setName(report.getName());
        reportDTO.setContent(report.getComment());
        reportDTO.setCreatedAt(report.getCreatedDate());
        reportDTO.setPharmHospId(report.getPharmHospId());
        reportDTO.setReportType(report.getReportType());
        reportDTO.setIsChecked(report.getIsChecked());
        reportDTO.setAuthor(report.getReport_member().getName()); // 최적화 여지 생각해보기
        if(report.getModifiedAt() != null)
            reportDTO.setModifiedAt(report.getModifiedAt());
        try{
            reportDTO.setTargetName(hospitalService.findById(report.getPharmHospId()).getName());
        } catch (Exception ignored){

        }
        try{
            reportDTO.setTargetName(pharmacyService.findById(report.getPharmHospId()).getName());
        } catch (Exception ignored){

        }
        return reportDTO;
    }

    private ReportDTOForAdmin getReportDTOForAdmin(Long reportId) {
        Report report = reportService.getReport(reportId);

        ReportDTOForAdmin reportDTOForAdmin = new ReportDTOForAdmin();
        reportDTOForAdmin.setName(report.getName());
        reportDTOForAdmin.setContent(report.getComment());
        reportDTOForAdmin.setCreatedAt(report.getCreatedDate());
        reportDTOForAdmin.setReportType(report.getReportType());
        reportDTOForAdmin.setIsChecked(report.getIsChecked());
        reportDTOForAdmin.setAuthor(report.getReport_member().getName()); // 최적화 여지 생각해보기
        if(report.getModifiedAt() != null)
            reportDTOForAdmin.setModifiedAt(report.getModifiedAt());
        reportDTOForAdmin.setIsChecked(report.getIsChecked());

        try{
            reportDTOForAdmin.setTargetName(hospitalService.findById(report.getPharmHospId()).getName());
        } catch (Exception ignored){

        }
        try{
            reportDTOForAdmin.setTargetName(pharmacyService.findById(report.getPharmHospId()).getName());
        } catch (Exception ignored){

        }

        reportDTOForAdmin.setPharmHospId(report.getPharmHospId());
        return reportDTOForAdmin;
    }

}
