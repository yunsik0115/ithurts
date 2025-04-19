package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.controller.REST.jsonDTO.CommentJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.LoveJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.MemberJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.StatusCode;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.CommentJSON;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.LoveJSON;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.UserJSON;
import com.sidepj.ithurts.domain.Comment;
import com.sidepj.ithurts.domain.Love;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.service.CommentService;
import com.sidepj.ithurts.service.LoveService;
import com.sidepj.ithurts.service.MemberService;
import com.sidepj.ithurts.service.SessionConst;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/json/members")
@RequiredArgsConstructor
public class MemberJSONController {

    private final MemberService memberService;
    private final CommentService commentService;
    private final LoveService loveService;

    // Create in LoginJsonController Signup Method.

    @GetMapping("/{memberId}") // Read
    @ResponseBody
    public ResponseEntity<MemberJsonResponse> getAccountInfoJson(@PathVariable Long memberId, HttpServletRequest request) throws Exception{
        Member memberById = memberService.getMemberById(memberId);
        isAlreadyLoginAndUserValidation(request, memberById.getId());
        UserJSON userJSON = userJSONFromEntity(memberById);
        return new ResponseEntity<>(new MemberJsonResponse(StatusCode.OK, "성공 : 유저 정보 가져오기", userJSON), HttpStatus.OK);
    }

    @PatchMapping("/{memberId}")
    @ResponseBody
    public ResponseEntity<MemberJsonResponse> modifyAccountInfoJson // UPDATE
            (@PathVariable Long memberId, @RequestBody MemberJoinDTO memberJoinDTO,
             HttpServletRequest request) throws Exception{
        isAlreadyLoginAndUserValidation(request, memberId);
        Member changedMember = memberService.updateMemberById(memberId, memberJoinDTO);
        UserJSON userJSON = userJSONFromEntity(changedMember);
        return new ResponseEntity<>(new MemberJsonResponse(StatusCode.OK, "성공 : 유저 정보 업데이트", userJSON), HttpStatus.OK);
    }

    @DeleteMapping("/{memberId}") // DELETE
    public String removeAccountJson(@PathVariable Long memberId, HttpServletRequest request) throws Exception{
        isAlreadyLoginAndUserValidation(request, memberId);
        memberService.deleteAccount(memberId);
        return "OK";
    }

    @GetMapping("/{memberId}/loves")
    public ResponseEntity<LoveJsonResponse> getLovesByMember(@PathVariable Long memberId, Pageable pageable){
        List<Love> lovesByMemberId = loveService.findLovesByMemberId(memberId);
        List<LoveJSON> loveJSONList = loveJSONListFromEntity(lovesByMemberId);

        LoveJsonResponse loveJsonResponse = new LoveJsonResponse(StatusCode.OK, "성공 : 유저가 누른 좋아요를 성공적으로 불러왔습니다", loveJSONList);

        return new ResponseEntity<>(loveJsonResponse, HttpStatus.OK);
    }

    @GetMapping("/{memberId}/comments")
    public ResponseEntity<CommentJsonResponse> getCommentsByMember(@PathVariable Long memberId, Pageable pageable){
        List<Comment> commentsByUser = commentService.getCommentsByUser(memberId);
        List<CommentJSON> commentJSONList = commentJSONListFromEntity(commentsByUser);

        CommentJsonResponse commentJsonResponse = new CommentJsonResponse(StatusCode.OK, "성공 : 유저가 작성한 댓글 목록을 성공적으로 불러왔습니다", commentJSONList);
        return new ResponseEntity<>(commentJsonResponse, HttpStatus.OK);
    }


    // 유저의 로그인 상태를 검증하고, 다른 유저가 특정 유저 정보에 접근하여 업데이트를 시도하는 경우 차단함.
    private static void isAlreadyLoginAndUserValidation(HttpServletRequest request, Long memberId) throws Exception {
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null){
            throw new AccessDeniedException("로그인이 필요합니다");
        }

        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);
        if(!member.getId().equals(memberId)){
            throw new IllegalAccessException("잘못된 접근입니다");
        }
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

    private List<LoveJSON> loveJSONListFromEntity(List<Love> loves){
        List<LoveJSON> loveJSONList = new ArrayList<>();

        for (Love love : loves) {
            loveJSONList.add(loveJSONFromEntity(love));
        }
        return loveJSONList;
    }

    private LoveJSON loveJSONFromEntity(Love love){
        LoveJSON loveJSON = new LoveJSON();
        loveJSON.setId(love.getId());
        if(love.getPost().getId() != null)
        loveJSON.setPostId(love.getPost().getId());
        if(love.getComment().getId() != null)
        loveJSON.setCommentId(love.getComment().getId());
        loveJSON.setAuthor(love.getLove_member().getName());
        return loveJSON;
    }

    private CommentJSON commentJSONFromEntity(Comment comment){
        CommentJSON commentJSON = new CommentJSON();
        commentJSON.setId(comment.getId());
        commentJSON.setContent(comment.getContent());
        commentJSON.setAuthor(comment.getAuthor().getName());
        commentJSON.setLovesCount(comment.getLoves().size());
        return commentJSON;
    }

    private List<CommentJSON> commentJSONListFromEntity(List<Comment> comments){
        List<CommentJSON> commentJSONList = new ArrayList<>();
        for (Comment comment : comments) {
            commentJSONList.add(commentJSONFromEntity(comment));
        }
        return commentJSONList;
    }

}
