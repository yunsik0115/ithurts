package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void removePost(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Post getPost(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다"));
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPostByName(String name) {
        return postRepository.findPostsByPostName(name);
    }

    @Override
    public List<Post> getPostByMember(String memberName) {
        return postRepository.findPostsByMemberName(memberName);
    }

    @Override
    public List<Post> getPostByContent(String content) {
        return postRepository.findPostsByContent(content);
    }

    @Override
    public List<Post> getPostByPostType(String postType) {
        return postRepository.findPostsByPostType(postType);
    }

}
