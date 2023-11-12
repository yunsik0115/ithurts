package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Comment;
import com.sidepj.ithurts.domain.Love;
import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.service.dto.PostDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostService {

    PostDTO savePost(Post post);

    PostDTO getPost(Long id);

    void removePost(Post post);

    List<PostDTO> getAllPosts();

    List<PostDTO> getPostByName(String name);

    List<PostDTO> getPostByMember(String memberName);

    List<PostDTO> getPostByContent(String content);

    List<PostDTO> getPostByPostType(String postType);



}
