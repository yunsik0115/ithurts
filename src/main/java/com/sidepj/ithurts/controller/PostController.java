package com.sidepj.ithurts.controller;

import com.sidepj.ithurts.domain.Comment;
import com.sidepj.ithurts.domain.Love;
import com.sidepj.ithurts.repository.LoveRepository;
import com.sidepj.ithurts.service.CommentService;
import com.sidepj.ithurts.service.PostService;
import com.sidepj.ithurts.service.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final LoveRepository loveRepository;

    @GetMapping("/posts")
    public String dashboard(Model model) {
        List<PostDTO> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "posts.html";
    }

    @GetMapping("/posts/search")
    public String searchPostByName(@RequestParam(required = false) String name, Model model) {
        List<PostDTO> posts = postService.getPostByName(name);
        model.addAttribute("posts", posts);
        return "posts.html";
    }

    @GetMapping("/posts/search")
    public String searchPostByContent(@RequestParam(required = false) String content, Model model) {

        List<PostDTO> posts = postService.getPostByContent(content);
        model.addAttribute("posts", posts);
        return "posts.html";
    }

    @GetMapping("/posts/search")
    public String searchPostByAuthor(@RequestParam(required = false) String author, Model model) {
        List<PostDTO> posts = postService.getPostByMember(author);
        model.addAttribute("posts", posts);
        return "posts.html";
    }

    @GetMapping("/post")
    public String postForm() {
        return "postForm.html";
    }

    @PostMapping("/post")
    public String writePost(@ModelAttribute PostDTO postDTO, Model model) {
        return "postForm.html";
    }

    @PatchMapping("/post/{postId}")
    public String modifyPost(@PathVariable Long postId, Model model) {
        PostDTO post = postService.getPost(postId);
        model.addAttribute("post", post);
        return "posts.html"; // will be redirected to getPost()
    }

    @GetMapping("/post/{postId}")
    public String getPost(@PathVariable Long postId, Model model){
        PostDTO post = postService.getPost(postId);
        List<Comment> comments = commentService.getCommentsByPost(postId);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        return "post.html";
    }

    @DeleteMapping("/post/{postId}")
    public String deletePost(@PathVariable Long postId){
        postService.removePost(postId);
        return "redirect:/posts.html"; // will be redirected to dashboard()
    }

    @GetMapping("/post/{postId}/like")
    public String getLikesOnThisPost(@PathVariable Long postId, Model model){
        List<Love> likes = loveRepository.findByPostId(postId);
        int count = likes.size();
        model.addAttribute("likes", likes);
        model.addAttribute("count", count);
        return "post.html";
    }

    @PostMapping("/post/{postId}/like")
    public String addLikeOnThisPost(){
        return "post.html";
    }

    @DeleteMapping("/post/{postId}/like/{likeId}")
    public String removeLikeOnThisPost(@PathVariable Long postId,
                                       @PathVariable(name = "likeId") Long loveId){
        loveRepository.removeLoveById(loveId);
        return "post.html";
    }

//    @GetMapping("/post/{postId}/comment"){
//        무필요, post 불러올때 같이 가져옴
//    }
    @PostMapping("/post/{postId}/comment")
    public String addCommentOnThisPost(){
        return "post.html";
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public String deleteCommentOnThisPost(){
        return "post.html";
    }

    @GetMapping("/post/{postId}/report")
    public String postReportForm(){
        return "report.html";
    }

    @PostMapping("/post/{postId}/report")
    public String postReport(){
        return "report.html";
    }

    @GetMapping("/post/{postId}/comment/{commentId}/report")
    public String commentReportForm(){
        return "commentReport.html";
    }

    @PostMapping("/post/{postId}/comment/{commentId}/report")
    public String commentReport(){
        return "commentReport.html";
    }




}
