package com.sidepj.ithurts.service;

import com.sidepj.ithurts.controller.REST.jsonDTO.data.PostJSON;
import com.sidepj.ithurts.domain.Comment;
import com.sidepj.ithurts.domain.Love;
import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.service.dto.PostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Post savePost(Post post);

    Post getPost(Long id);

    void removePost(Long id);

    List<Post> getAllPosts();

    Page<Post> getAllPosts(Pageable pageable);

    List<Post> getPostByName(String name);

    Page<Post> getPostByName(String name, Pageable pageable);

    List<Post> getPostByMember(String memberName);

    Page<Post> getPostByMember(String memberName, Pageable pageable);

    List<Post> getPostByContent(String content);

    Page<Post> getPostByContent(String content, Pageable pageable);

    List<Post> getPostByPostType(String postType);

    Page<Post> getPostByPostType(String postType, Pageable pageable);

    void updatePostEntityFromDTO(Long id, PostJSON postJSON);

}
