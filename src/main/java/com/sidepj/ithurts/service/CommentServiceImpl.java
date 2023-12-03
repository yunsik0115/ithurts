package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Comment;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.repository.CommentRepository;
import com.sidepj.ithurts.repository.MemberRepository;
import com.sidepj.ithurts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final MemberService memberService;

    @Override
    public Comment saveComment(Long postId, Long memberId , Comment comment) {
        Member commentAuthor = memberService.getMemberById(memberId);
        comment.setAuthor(commentAuthor);

        Post post = postService.getPost(postId);
        comment.setPost(post);

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public List<Comment> getCommentsByUser(Long userId) {
        return commentRepository.findByAuthorId(userId);
    }

    @Override
    public void deleteComment(Long commentId, Long memberId) throws Exception {
        Member member = memberService.getMemberById(memberId);
        Comment commentToBeDeleted = getCommentById(commentId);

        if(!commentToBeDeleted.getAuthor().equals(member)){
            throw new AccessDeniedException("작성한 회원만이 댓글을 삭제할 수 있습니다");
        }

        commentRepository.removeCommentById(commentId);
    }

    @Override
    public Comment getCommentById(Long commentId) {
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if(findComment.isEmpty()){
            throw new IllegalArgumentException("해당 댓글을 찾을 수 없습니다");
        }
        return findComment.get();
    }
}
