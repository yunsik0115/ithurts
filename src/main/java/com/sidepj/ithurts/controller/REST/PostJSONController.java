package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.service.CommentService;
import com.sidepj.ithurts.service.LoveService;
import com.sidepj.ithurts.service.PostService;
import com.sidepj.ithurts.service.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/json/dashboard")
public class PostJSONController {

    private final PostService postService;
    private final CommentService commentService;
    private final LoveService loveService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/posts")
    @ResponseBody
    public List<PostDTO> postsJSON() {
        return postService.getAllPosts();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/posts/search")
    @ResponseBody
    public List<PostDTO> searchPostByNameJSON(@RequestParam(required = false) String name) {
        return postService.getPostByName(name);
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/post")
    @ResponseBody
    public PostDTO writePostJSON(@ModelAttribute PostDTO postDTO) {
        return postService.savePost(postDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/post/{postId}")
    @ResponseBody
    public PostDTO getPostJSON(@PathVariable Long postId){
        return postService.getPost(postId);
    }

    @DeleteMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity<PostDTO> deletePostJson(@PathVariable Long postId){
        postService.removePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
