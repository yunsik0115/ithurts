package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.controller.REST.jsonDTO.PostJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.PostsJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.StatusCode;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.CommentJSON;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.PostJSON;
import com.sidepj.ithurts.domain.Comment;
import com.sidepj.ithurts.domain.Love;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.repository.PostRepository;
import com.sidepj.ithurts.service.CommentService;
import com.sidepj.ithurts.service.LoveService;
import com.sidepj.ithurts.service.PostService;
import com.sidepj.ithurts.service.SessionConst;
import com.sidepj.ithurts.service.dto.PostDTO;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
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

    @GetMapping("/posts")
    @ResponseBody
    public ResponseEntity<PostsJsonResponse> postsJSON(Pageable pageable) {
        List<Post> allPosts = postService.getAllPosts();
        List<PostJSON> postJSONList = postJSONListFromEntity(allPosts);
        PostsJsonResponse postsJsonResponse = new PostsJsonResponse(StatusCode.OK, "성공 : 게시판의 글들을 불러오기", postJSONList);
        return new ResponseEntity<>(postsJsonResponse, HttpStatus.OK);
    }

    @GetMapping("/posts/search")
    @ResponseBody
    public ResponseEntity<PostsJsonResponse> searchPostByNameJSON(@RequestParam(required = false) String name, Pageable pageable) {
        List<Post> postByName = postService.getPostByName(name);
        List<PostJSON> postJSONList = postJSONListFromEntity(postByName);
        PostsJsonResponse postsJsonResponse = new PostsJsonResponse(StatusCode.OK, "성공 : 검색한 이름의 게시글 불러오기", postJSONList);
        return new ResponseEntity<>(postsJsonResponse, HttpStatus.OK);
    }


    @PostMapping("/post")
    @ResponseBody // create
    public ResponseEntity<PostJsonResponse> writePostJSON(@RequestBody PostDTO postDTO, HttpServletRequest request) throws Exception{
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new AccessDeniedException("로그인 한 사용자만 이용할 수 있습니다");
        }
        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);
        Post post = new Post(postDTO);
        post.setPostMember(member);
        Post savedPost = postService.savePost(post);
        PostJSON postJSON = postJSONFromEntity(savedPost);
        PostJsonResponse postJsonResponse = new PostJsonResponse(StatusCode.CREATED, "성공 : 작성된 게시글이 성공적으로 업로드되었습니다", postJSON);
        return new ResponseEntity<>(postJsonResponse, HttpStatus.CREATED);
    }


    @GetMapping("/post/{postId}")
    @ResponseBody // read
    public ResponseEntity<PostJsonResponse> getPostJSON(@PathVariable Long postId){
        Post post = postService.getPost(postId);
        PostJSON postJSON = postJSONFromEntity(post);
        // POST의 내용과 댓글, 좋아요 카운팅만 갖고 있다
        PostJsonResponse postJsonResponse = new PostJsonResponse(StatusCode.OK, "성공 : 작성된 id의 게시글을 성공적으로 불러왔습니다", postJSON);
        postJsonResponse.setCommentJSON(commentJSONListFromEntity(post));
        return new ResponseEntity<>(postJsonResponse, HttpStatus.OK);
    }

    @PatchMapping("/post/{postId}")
    @ResponseBody
    public ResponseEntity<PostJsonResponse> updatePostJSON(@PathVariable Long postId, @RequestBody PostJSON postJSON, HttpServletRequest request) throws AccessDeniedException {

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

        postService.updatePostEntityFromDTO(postId, postJSON);
        Post post = postService.getPost(postId);
        return new ResponseEntity<>(new PostJsonResponse(StatusCode.OK,"성공적으로 ID의 게시글을 업데이트 했습니다", postJSONFromEntity(post)), HttpStatus.OK);
    }


    @DeleteMapping("/post/{postId}")
    @ResponseBody // Delete
    public ResponseEntity<PostJsonResponse> deletePostJson(@PathVariable Long postId, HttpServletRequest request) throws Exception{

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
        PostJsonResponse postJsonResponse = new PostJsonResponse(StatusCode.OK, "성공 : 작성된 게시글이 성공적으로 삭제되었습니다");
        return new ResponseEntity<>(postJsonResponse, HttpStatus.OK);
    }


    // TO - DO LikeJsonResponse로 반환
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
    public ResponseEntity<PostJsonResponse> addLikeOnThisPost(@PathVariable Long postId, HttpServletRequest request) throws Exception{

        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new AccessDeniedException("로그인 한 사용자만 이용할 수 있습니다");
        }
        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);
        // 같은 유저가 동일 경로로 요청 2회 들어오는 경우 좋아요 취소로 간주하기 (하지 말자, 좋아요 취소시 나중에 Notification 로직 하나에 같이 들어가야한다)
        Love love = new Love();
        love.setLove_member(member);
        loveService.addLove(postId, love);

        Post post = postService.getPost(postId);
        PostJSON postJSON = postJSONFromEntity(post);
        PostJsonResponse postJsonResponse = new PostJsonResponse(StatusCode.CREATED, "성공 : 글 좋아요 등록", postJSON);
        return new ResponseEntity<>(postJsonResponse, HttpStatus.OK);
    }

    @DeleteMapping("/post/{postId}/like/{likeId}")
    @ResponseBody
    public ResponseEntity<PostJsonResponse> deleteLikeOnThisPost(@PathVariable Long postId, @PathVariable Long likeId, HttpServletRequest request) throws Exception{

        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new AccessDeniedException("로그인 한 사용자만 이용할 수 있습니다");
        }
        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);
        // 같은 유저가 동일 경로로 요청 2회 들어오는 경우 좋아요 취소로 간주하기 (하지 말자, 좋아요 취소시 나중에 Notification 로직 하나에 같이 들어가야한다)
        Love love = new Love();
        love.setLove_member(member);
        loveService.addLove(postId, love);

        Post post = postService.getPost(postId);
        PostJSON postJSON = postJSONFromEntity(post);
        PostJsonResponse postJsonResponse = new PostJsonResponse(StatusCode.CREATED, "성공 : 글 좋아요 취소", postJSON);
        return new ResponseEntity<>(postJsonResponse, HttpStatus.OK);
    }

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<PostJsonResponse> addCommentOnThisPost(@PathVariable Long postId, @RequestBody CommentJSON commentJSON , HttpServletRequest request) throws Exception{
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new AccessDeniedException("로그인 한 사용자만 이용할 수 있습니다");
        }
        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);
        Comment comment = new Comment();
        comment.setContent(commentJSON.getContent());
        commentService.saveComment(postId, member.getId(), comment);

        PostJsonResponse postJsonResponse = new PostJsonResponse(StatusCode.CREATED, "성공 : 댓글이 성공적으로 생성되었습니다");
        return new ResponseEntity<>(postJsonResponse, HttpStatus.OK);
    }

    @DeleteMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<PostJsonResponse> deleteCommentOnThisPost(@PathVariable Long postId, @PathVariable Long commentId, HttpServletRequest request) throws Exception{
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            throw new AccessDeniedException("로그인 한 사용자만 이용할 수 있습니다");
        }
        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);

        commentService.deleteComment(member.getId(), commentId);

        PostJsonResponse postJsonResponse = new PostJsonResponse(StatusCode.OK, "성공 : 댓글 삭제에 성공하였습니다");
        return new ResponseEntity<>(postJsonResponse, HttpStatus.OK);
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
