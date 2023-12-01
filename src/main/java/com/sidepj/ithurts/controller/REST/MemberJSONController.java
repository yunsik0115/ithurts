package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.controller.REST.jsonDTO.MemberJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.StatusCode;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.UserJSON;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.service.MemberService;
import com.sidepj.ithurts.service.SessionConst;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/json/members")
@RequiredArgsConstructor
public class MemberJSONController {

    private final MemberService memberService;

    // Create in LoginJsonController Signup Method.

    @GetMapping("/{memberId}") // Read
    @ResponseBody
    public ResponseEntity<MemberJsonResponse> getAccountInfoJson(@PathVariable Long memberId, HttpServletRequest request) throws Exception{
        isAlreadyLoginAndUserValidation(request, memberId);
        Member memberById = memberService.getMemberById(memberId);
        UserJSON userJSON = userJSONFromEntity(memberById);
        return new ResponseEntity<>(new MemberJsonResponse(StatusCode.OK, "성공 : 유저 정보 가져오기", userJSON), HttpStatus.OK);
    }

    @PatchMapping("/{memberId}")
    @ResponseBody
    public ResponseEntity<MemberJsonResponse> modifyAccountInfoJson // UPDATE
            (@PathVariable Long memberId, @RequestBody MemberJoinDTO memberJoinDTO,
             HttpServletRequest request, HttpServletResponse response) throws Exception{
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

}
