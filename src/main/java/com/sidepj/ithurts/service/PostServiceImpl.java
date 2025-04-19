package com.sidepj.ithurts.service;

import com.sidepj.ithurts.controller.REST.jsonDTO.data.PostJSON;
import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.repository.PostRepository;
import com.sidepj.ithurts.service.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Override
    public Post savePost(Post post) {
        Post save = postRepository.save(post);
        return save;
    }

    @Override
    public void removePost(Long postId) {
        Optional<Post> postForRemove = postRepository.findById(postId);
        postRepository.delete(postForRemove.get());
    }

    @Override
    public Post getPost(Long id) {
        Optional<Post> isPost = postRepository.findById(id);
        if(isPost.isPresent()){
            Post post = isPost.get();
            return post;
        }
        else{
            throw new IllegalArgumentException("검색된 Post가 없습니다");
        }
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts;
    }

    @Override
    public List<Post> getPostByName(String name) {
        Optional<List<Post>> postsByPostName = postRepository.findPostsByPostName(name);
        if(postsByPostName.isPresent()){
            return postsByPostName.get();
        }
        throw new IllegalArgumentException("No Search Result!");
    }

    @Override
    public List<Post> getPostByMember(String memberName) {
        Optional<List<Post>> postsByMemberName = postRepository.findPostsByMemberName(memberName);
        if(postsByMemberName.isPresent()){
            List<Post> posts = postsByMemberName.get();
            return posts;
        }
        throw new IllegalArgumentException("그런 이름을 가진 회원이 없습니다");
    }

    @Override
    public List<Post> getPostByContent(String content) {
        Optional<List<Post>> postsByContent = postRepository.findPostsByContent(content);
        if(postsByContent.isPresent()){
            List<Post> posts = postsByContent.get();
            return posts;
        }
        throw new IllegalArgumentException("그런 내용을 가진 글이 없습니다");
    }

    @Override
    public List<Post> getPostByPostType(String postType) {
        Optional<List<Post>> postsByPostType = postRepository.findPostsByPostType(postType);
        if(postsByPostType.isPresent()){
            List<Post> posts = postsByPostType.get();
            return posts;
        }
        throw new IllegalArgumentException("그런 타입의 글이 없습니다");
    }

    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        return postRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Post> getPostByName(String name, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        return postRepository.findPostsByPostName(name,PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }

    @Override
    public Page<Post> getPostByMember(String memberName, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        return postRepository.findPostsByMemberNameDesc(memberName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }

    @Override
    public Page<Post> getPostByContent(String content, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        return postRepository.findPostsByContent(content, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }

    @Override
    public Page<Post> getPostByPostType(String postType, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        return postRepository.findPostsByPostType(postType, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }

    @Override
    public void updatePostEntityFromDTO(Long id, PostJSON dto) {
        // 엔티티를 ID로 찾음
        Post entity = getPost(id);

        // 게시글 이름이 null이 아니거나 공백이 아닌 경우에만 업데이트
        if (dto.getPostName() != null && !dto.getPostName().trim().isEmpty()) {
            entity.setPostName(dto.getPostName());
        }

        // 게시글 내용이 null이 아니거나 공백이 아닌 경우에만 업데이트
        if (dto.getContent() != null && !dto.getContent().trim().isEmpty()) {
            entity.setContent(dto.getContent());
        }

        // 게시글 타입이 null이 아니거나 공백이 아닌 경우에만 업데이트
        if (dto.getPostType() != null && !dto.getPostType().trim().isEmpty()) {
            entity.setPostType(dto.getPostType());
        }

        // 업데이트 시간 설정 (수정된 날짜)
        entity.setModifiedDate(LocalDateTime.now());
    }
}
