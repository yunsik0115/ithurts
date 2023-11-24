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
    public PostDTO savePost(PostDTO postDTO) {
        Post save = postRepository.save(new Post(postDTO));
        return new PostDTO(save);
    }

    @Override
    public void removePost(Long postId) {
        Optional<Post> postForRemove = postRepository.findById(postId);
        postRepository.delete(postForRemove.get());
    }

    @Override
    public PostDTO getPost(Long id) {
        Optional<Post> isPost = postRepository.findById(id);
        if(isPost.isPresent()){
            Post post = isPost.get();
            return new PostDTO(post);
        }
        else{
            throw new IllegalArgumentException("검색된 Post가 없습니다");
        }
    }

    @Override
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> allDTO = new ArrayList<>();
        for (Post post : posts) {
            PostDTO postDTO = new PostDTO(post);
            //postDTO.setCommentCount(post.getComments().size()); if postDTO Constructor injection fail then initalize the field into 0
            //postDTO.setLoveCount(post.getLoves().size()); retrieve counts data later.
            allDTO.add(postDTO);
        }
        return allDTO;
    }

    @Override
    public List<PostDTO> getPostByName(String name) {
        Optional<List<Post>> postsByPostName = postRepository.findPostsByPostName(name);
        if(postsByPostName.isPresent()){
            List<Post> posts = postsByPostName.get();
            List<PostDTO> postDTOS = new ArrayList<>();
            for (Post post : posts) {
                postDTOS.add(new PostDTO(post));
            }
            return postDTOS;
        }
        throw new IllegalArgumentException("No Search Result!");
    }

    @Override
    public List<PostDTO> getPostByMember(String memberName) {
        Optional<List<Post>> postsByMemberName = postRepository.findPostsByMemberName(memberName);
        if(postsByMemberName.isPresent()){
            List<Post> posts = postsByMemberName.get();
            List<PostDTO> postDTOS = new ArrayList<>();
            for (Post post : posts) {
                postDTOS.add(new PostDTO(post));
            }
            return postDTOS;
        }
        throw new IllegalArgumentException("그런 이름을 가진 회원이 없습니다");
    }

    @Override
    public List<PostDTO> getPostByContent(String content) {
        Optional<List<Post>> postsByContent = postRepository.findPostsByContent(content);
        if(postsByContent.isPresent()){
            List<Post> posts = postsByContent.get();
            List<PostDTO> postDTOS = new ArrayList<>();
            for (Post post : posts) {
                postDTOS.add(new PostDTO(post));
            }
            return postDTOS;
        }
        throw new IllegalArgumentException("그런 내용을 가진 글이 없습니다");
    }

    @Override
    public List<PostDTO> getPostByPostType(String postType) {
        Optional<List<Post>> postsByPostType = postRepository.findPostsByPostType(postType);
        if(postsByPostType.isPresent()){
            List<Post> posts = postsByPostType.get();
            List<PostDTO> postDTOS = new ArrayList<>();
            for (Post post : posts) {
                postDTOS.add(new PostDTO(post));
            }
            return postDTOS;
        }
        throw new IllegalArgumentException("그런 타입의 글이 없습니다");
    }

}
