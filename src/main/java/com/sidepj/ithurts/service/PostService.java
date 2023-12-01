package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Comment;
import com.sidepj.ithurts.domain.Love;
import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.service.dto.PostDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Post savePost(Post post);

    Post getPost(Long id);

    void removePost(Long id);

    List<Post> getAllPosts();

    List<Post> getPostByName(String name);

    List<Post> getPostByMember(String memberName);

    List<Post> getPostByContent(String content);

    List<Post> getPostByPostType(String postType);



}
