package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.domain.Love;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.repository.PostRepository;
import com.sidepj.ithurts.service.CommentService;
import com.sidepj.ithurts.service.LoveService;
import com.sidepj.ithurts.service.PostService;
import com.sidepj.ithurts.service.SessionConst;
import com.sidepj.ithurts.service.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/json/dashboard")
public class PostJSONController {

    private final PostRepository postRepository;
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
    public PostDTO writePostJSON(@RequestBody PostDTO postDTO, HttpServletRequest request) throws Exception{
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new AccessDeniedException("로그인 한 사용자만 이용할 수 있습니다");
        }
        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);
        Post post = new Post(postDTO);
        post.setPostMember(member);
        return postService.savePost(post);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/post/{postId}")
    @ResponseBody
    public PostDTO getPostJSON(@PathVariable Long postId){
        return postService.getPost(postId);
    }


    @DeleteMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity<PostDTO> deletePostJson(@PathVariable Long postId, HttpServletRequest request) throws Exception{

        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new AccessDeniedException("로그인 한 사용자만 이용할 수 있습니다");
        }
        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);

        Optional<Post> postOptional = postRepository.findById(postId);

        if(postOptional.isEmpty()){
            throw new AccessDeniedException("해당 글이 존재하지 않아 삭제가 불가능합니다");
        }

        Post targetPost = postOptional.get();
        if(!targetPost.getPostMember().getId().equals(member.getId())){
            throw new AccessDeniedException("글은 게시자만 지울 수 있습니다");
        }

        postService.removePost(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/post/{postId}/like")
    @ResponseBody
    public List<Love> getLikesOnThisPost(@PathVariable Long postId){
        List<Love> likes = loveService.getLoves(postId);
        return likes;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/post/{postId}/like")
    @ResponseBody
    public ResponseEntity<Love> addLikeOnThisPost(@PathVariable Long postId, HttpServletRequest request) throws Exception{

        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new AccessDeniedException("로그인 한 사용자만 이용할 수 있습니다");
        }
        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);

        Love love = new Love();
        love.setLove_member(member);
        loveService.addLove(postId, love);

        // Member Data Injection (maybe by using cookie? or Session?)
        // HOW to add entity if we get DTO as (POST) findbyPostID?
        return new ResponseEntity<Love>(HttpStatus.OK);
    }


}
