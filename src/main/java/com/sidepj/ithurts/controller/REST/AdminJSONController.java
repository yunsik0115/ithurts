package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.controller.REST.jsonDTO.*;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.*;
import com.sidepj.ithurts.controller.dto.ReportDTOForAdmin;
import com.sidepj.ithurts.domain.*;
import com.sidepj.ithurts.repository.PostRepository;
import com.sidepj.ithurts.service.DataService;
import com.sidepj.ithurts.service.MemberService;
import com.sidepj.ithurts.service.PostService;
import com.sidepj.ithurts.service.ReportService;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import com.sidepj.ithurts.service.dto.PostDTO;
import com.sidepj.ithurts.service.dto.jsonparsingdto.HospitalDTO;
import com.sidepj.ithurts.service.dto.jsonparsingdto.PharmacyDTO;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
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
    private final PostRepository postRepository;

    // TO - DO 일관성 있는 naming 규칙 적용 필요 user <-> member 불일치 등등

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public ResponseEntity<MembersJsonResponse> getAllAccountInfo(@PageableDefault(size = 12, sort="id", direction = Sort.Direction.DESC) Pageable pageable){

//        List<Member> members = memberService.getMembers();
//        MembersJsonResponse membersJsonResponse = new MembersJsonResponse(StatusCode.OK, "성공 : 전체 멤버 조회하기", userJSONListFromUserJSON(members));
//        return new ResponseEntity<>(membersJsonResponse, HttpStatus.OK);
        Page<Member> membersPage = memberService.getMembers(pageable);
        List<Member> members = membersPage.getContent();
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
        reportService.remove(reportId);
        return "OK";
    }

    @GetMapping("/metainfo/hospitals")
    public ResponseEntity<HospitalJsonResponse> getAllHospitals(Pageable pageable){
//        List<Hospital> hospitals = hospitalService.getAll();
//        HospitalJsonResponse hospitalJsonResponse = new HospitalJsonResponse(StatusCode.OK, "정상 응답 : 전체 병원 목록 불러오기", createHospitalJsonListFromEntites(hospitals));
//        return new ResponseEntity<>(hospitalJsonResponse, HttpStatus.OK);
        Page<Hospital> allHospitals = hospitalService.getAll(pageable);
        HospitalJsonResponse hospitalJsonResponse = new HospitalJsonResponse(StatusCode.OK, "정상 응답 : 전체 병원 목록 불러오기", createHospitalJsonListFromEntites(allHospitals.getContent()));
        return new ResponseEntity<>(hospitalJsonResponse, HttpStatus.OK);
    }

    @GetMapping("/metainfo/hospitals/{hospitalId}")
    public ResponseEntity<HospitalJSON> getHospitalInfo(@PathVariable Long hospitalId){
        Hospital hospital = hospitalService.findById(hospitalId);
        HospitalJSON hospitalJsonFromEntity = createHospitalJsonFromEntity(hospital);
        return new ResponseEntity<>(hospitalJsonFromEntity,HttpStatus.OK);

    }

    @PatchMapping("/metainfo/hospitals/{hospitalId}")
    public ResponseEntity<HospitalJSON> updateHospitalInfo(@PathVariable Long hospitalId, @RequestBody HospitalDTO hospitalDTO) throws IllegalAccessException {
        hospitalService.updateEntityFromDTO(hospitalId, hospitalDTO);
        Hospital hospital = hospitalService.findById(hospitalId);
        return new ResponseEntity<>(createHospitalJsonFromEntity(hospital), HttpStatus.OK);
    }


    @DeleteMapping("/metainfo/hospitals/{hospitalId}")
    public String removeHospitalInfo(@PathVariable Long hospitalId){
        hospitalService.remove(hospitalId);
        return "OK";
    }

    @PostMapping("/metainfo/hospitals/retrieve")
    public ResponseEntity<HospitalJsonResponse> retrieveAllHospitals(){
        List<Hospital> hospitals = hospitalService.retrieveAll();
        HospitalJsonResponse hospitalJsonResponse = new HospitalJsonResponse(StatusCode.OK, "전체 정보를 가져오는데 성공했습니다 : OPENAPI", createHospitalJsonListFromEntites(hospitals));
        return new ResponseEntity<>(hospitalJsonResponse, HttpStatus.OK);
    }

    @GetMapping("/metainfo/pharmacies")
    public ResponseEntity<PharmacyJsonResponse> getAllPharmacies(){
        List<Pharmacy> pharmacies = pharmacyService.getAll();
        PharmacyJsonResponse pharmacyJsonResponse = new PharmacyJsonResponse(StatusCode.OK, "전체 정보를 가져오는데 성공했습니다 : Local", createPharmacyJsonListFromEntites(pharmacies));
        return new ResponseEntity<>(pharmacyJsonResponse, HttpStatus.OK);
    }


    @GetMapping("/metainfo/pharmacies/{pharmacyId}")
    public ResponseEntity<PharmacyJSON> getPharmacyInfo(Long pharmacyId){
        Pharmacy pharmacy = pharmacyService.findById(pharmacyId);
        PharmacyJSON pharmacyJsonFromEntity = createPharmacyJsonFromEntity(pharmacy);
        return new ResponseEntity<>(pharmacyJsonFromEntity,HttpStatus.OK);
    }

    @PatchMapping("/metainfo/pharmacies/{pharmacyId}")
    public ResponseEntity<PharmacyJSON> updatePharmacyInfo(@PathVariable Long pharmacyId, @RequestBody PharmacyDTO pharmacyDTO) throws IllegalAccessException {
        pharmacyService.updateEntityFromDTO(pharmacyId, pharmacyDTO);
        Pharmacy pharmacy = pharmacyService.findById(pharmacyId);
        return new ResponseEntity<>(createPharmacyJsonFromEntity(pharmacy), HttpStatus.OK);
    }


    @DeleteMapping("/metainfo/pharmacies/{pharmacyId}")
    public String removePharmacyInfo(Long pharmacyId){
        pharmacyService.remove(pharmacyId);
        return "OK";
    }

    @PostMapping("/metainfo/pharmacies/retrieve")
    public String retrieveAllPharmacies(){
        pharmacyService.retrieveAll();
        return "OK";
    }

    @GetMapping("/posts")
    public PostsJsonResponse getPosts(Pageable pageable){
//        List<Post> allPosts = postService.getAllPosts();
//        List<PostJSON> postJSONS = postJSONListFromEntity(allPosts);
//        return new ResponseEntity<>(postJSONS, HttpStatus.OK);
        Page<Post> allPosts = postService.getAllPosts(pageable);
        List<PostJSON> postJSONS = postJSONListFromEntity(allPosts.getContent());
        return new PostsJsonResponse(StatusCode.OK, "정상적으로 Post를 불러왔습니다", postJSONS);
    }

    @GetMapping("/post/{postId}")
    public PostJsonResponse getPost(@PathVariable Long postId){
        Post post = postService.getPost(postId);
        PostJSON postJSON = postJSONFromEntity(post);
        return new PostJsonResponse(StatusCode.OK, "성공적으로 Post를 불러왔습니다", postJSON);
    }

    @PostMapping("/post")
    public PostJsonResponse addPost(@RequestBody PostDTO postDTO){
        Post post = new Post(postDTO);
        Post savedPost = postService.savePost(post);
        PostJSON postJSON = postJSONFromEntity(savedPost);
        return new PostJsonResponse(StatusCode.OK, "성공적으로 Post를 등록했습니다", postJSON);
    }

    @PatchMapping("/post/{postId}")
    public PostJsonResponse modifyPost(@RequestBody PostJSON postJson, @PathVariable Long postId) throws IllegalAccessException {
        postService.updatePostEntityFromDTO(postId, postJson);
        Post post = postService.getPost(postId);
        PostJSON postJSON = postJSONFromEntity(post);
        return new PostJsonResponse(StatusCode.OK, "성공적으로 Post를 수정했습니다", postJSON);
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable Long postId){
        postService.removePost(postId);
        return "OK";
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

    private List<HospitalJSON> createHospitalJsonListFromEntites(List<Hospital> hospitals){
        List<HospitalJSON> hospitalJsons = new ArrayList<>();
        for (Hospital hospital : hospitals) {
            hospitalJsons.add(createHospitalJsonFromEntity(hospital));
        }
        return hospitalJsons;
    }

    private HospitalJSON createHospitalJsonFromEntity(Hospital hospital){
        HospitalJSON hospitalJSON = new HospitalJSON();
        hospitalJSON.setHospitalId(hospital.getId());
        hospitalJSON.setName(hospital.getName());
        hospitalJSON.setHospitalType(hospital.getHospitalType());
        hospitalJSON.setCreatedDate(hospital.getCreatedDate());
        if(hospital.getUpdatedDate() != null){
            hospitalJSON.setUpdateDate(hospital.getUpdatedDate());
        }
        injectHospitalOfficeTimes(hospital, hospitalJSON);
        return hospitalJSON;
    }

    private void injectPharmacyOfficeTimes(Pharmacy pharmacy, PharmacyJSON pharmacyJson){
        List<PharmacyOfficeTime> officeTimes = pharmacy.getOfficeTimes();
        for (PharmacyOfficeTime officeTime : officeTimes) {
            if(officeTime.getWeekday().equals("monday")){
                pharmacyJson.setMonOpen(officeTime.getStartOffice());
                pharmacyJson.setMonClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("tuesday")){
                pharmacyJson.setTueOpen(officeTime.getStartOffice());
                pharmacyJson.setTueClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("wednesday")){
                pharmacyJson.setWedOpen(officeTime.getStartOffice());
                pharmacyJson.setWedClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("thursday")){
                pharmacyJson.setThuOpen(officeTime.getStartOffice());
                pharmacyJson.setThuClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("friday")){
                pharmacyJson.setFriOpen(officeTime.getStartOffice());
                pharmacyJson.setFriClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("satsun")){
                pharmacyJson.setSatSunOpen(officeTime.getStartOffice());
                pharmacyJson.setSatSunClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("holiday")){
                pharmacyJson.setHolidayOpen(officeTime.getStartOffice());
                pharmacyJson.setHolidayClosed(officeTime.getEndOffice());
            }
        }
    }

    private List<PharmacyJSON> createPharmacyJsonListFromEntites(List<Pharmacy> pharmacies){
        List<PharmacyJSON> pharmacyJSONS = new ArrayList<>();
        for (Pharmacy pharmacy : pharmacies) {
            pharmacyJSONS.add(createPharmacyJsonFromEntity(pharmacy));
        }
        return pharmacyJSONS;
    }

    private PharmacyJSON createPharmacyJsonFromEntity(Pharmacy pharmacy){
        PharmacyJSON pharmacyJSON = new PharmacyJSON();
        pharmacyJSON.setPharmacyId(pharmacy.getId());
        pharmacyJSON.setName(pharmacy.getName());
        pharmacyJSON.setCreatedDate(pharmacy.getCreatedDate());
        if(pharmacy.getUpdatedDate() != null){
            pharmacyJSON.setUpdateDate(pharmacy.getUpdatedDate());
        }
        injectPharmacyOfficeTimes(pharmacy, pharmacyJSON);
        return pharmacyJSON;
    }

    private void injectHospitalOfficeTimes(Hospital hospital, HospitalJSON hospitalJSON){
        List<HospitalOfficeTime> officeTimes = hospital.getOfficeTimes();
        for (HospitalOfficeTime officeTime : officeTimes) {
            if(officeTime.getWeekday().equals("monday")){
                hospitalJSON.setMonOpen(officeTime.getStartOffice());
                hospitalJSON.setMonClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("tuesday")){
                hospitalJSON.setTueOpen(officeTime.getStartOffice());
                hospitalJSON.setTueClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("wednesday")){
                hospitalJSON.setWedOpen(officeTime.getStartOffice());
                hospitalJSON.setWedClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("thursday")){
                hospitalJSON.setThuOpen(officeTime.getStartOffice());
                hospitalJSON.setThuClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("friday")){
                hospitalJSON.setFriOpen(officeTime.getStartOffice());
                hospitalJSON.setFriClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("satsun")){
                hospitalJSON.setSatSunOpen(officeTime.getStartOffice());
                hospitalJSON.setSatSunClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("holiday")){
                hospitalJSON.setHolidayOpen(officeTime.getStartOffice());
                hospitalJSON.setHolidayClosed(officeTime.getEndOffice());
            }
        }
    }

    private PostJSON postJSONFromEntity(Post post){
        PostJSON postJSON = new PostJSON();
        postJSON.setId(post.getId());
        postJSON.setPostName(post.getPostName());
        postJSON.setContent(post.getContent());
        postJSON.setAuthor(post.getPostMember().getName());
        postJSON.setCreatedDate(post.getCreatedDate());
        if(post.getModifiedDate() != null){
            postJSON.setModifiedDate(post.getModifiedDate());
        }
        if(post.getLoves() != null){
            postJSON.setLovesCount(post.getLoves().size());
        } else{
            postJSON.setLovesCount(0);
        }
        if(post.getComments() != null){
            postJSON.setCommentsCount(post.getComments().size());
        } else{
            postJSON.setCommentsCount(0);
        }
        return postJSON;
    }

    private List<PostJSON> postJSONListFromEntity(List<Post> posts){
        List<PostJSON> postJSONList = new ArrayList<>();
        for (Post post : posts) {
            postJSONList.add(postJSONFromEntity(post));
        }
        return postJSONList;
    }

    private List<CommentJSON> commentJSONListFromEntity(Post post){
        List<CommentJSON> commentJSONList = new ArrayList<>();
        List<Comment> comments = post.getComments();
        for (Comment comment : comments) {
            CommentJSON commentJSON = new CommentJSON();
            commentJSON.setPostId(post.getId());
            commentJSON.setId(comment.getId());
            commentJSON.setAuthor(comment.getAuthor().getName());
            commentJSON.setContent(comment.getContent());
            commentJSON.setLovesCount(comment.getLoves().size());
            commentJSONList.add(commentJSON);
        }
        return commentJSONList;
    }
}
