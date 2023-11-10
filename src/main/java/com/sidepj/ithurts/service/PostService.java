package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Comment;
import com.sidepj.ithurts.domain.Love;
import com.sidepj.ithurts.domain.Post;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostService {

    Post savePost(Post post);

    Post getPost(Long id);

    void removePost(Post post);

    List<Post> getAllPosts();

    List<Post> getPostByName(String name);

    List<Post> getPostByMember(String memberName);

    List<Post> getPostByContent(String content);

    List<Post> getPostByPostType(String postType);



}
