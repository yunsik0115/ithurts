package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.repository.PostRepository;
import com.sidepj.ithurts.service.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

}
