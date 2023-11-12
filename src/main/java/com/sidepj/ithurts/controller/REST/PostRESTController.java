package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.service.MemberService;
import com.sidepj.ithurts.service.PostService;
import com.sidepj.ithurts.service.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostRESTController{

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getPosts(){
        List<PostDTO> allPosts = postService.getAllPosts();
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }

}
