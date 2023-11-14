package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.service.dto.MemberDTO;
import com.sidepj.ithurts.service.dto.PostDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    @PersistenceContext
    EntityManager em;

    @Transactional
    @AfterEach
    public void execute() {
        em.flush();
        em.createNativeQuery("TRUNCATE TABLE " + "posts").executeUpdate();
        em.createNativeQuery("TRUNCATE TABLE " + "members").executeUpdate();
    }


    @Test
    void savePost() {
        MemberDTO author = new MemberDTO("mason", "xxxx", "User");
        Member join = memberService.join(author, author.getRole());
        Post testPost1 = new Post("test post 1", "test content 1", join);
        postService.savePost(testPost1);
    }

    @Test
    void getPost() {
        MemberDTO author = new MemberDTO("mason", "xxxx", "User");
        Member join = memberService.join(author, author.getRole());
        Post testPost1 = new Post("test post 1", "test content 1", join);
        PostDTO savedPost = postService.savePost(testPost1);
        Long savedPostId = savedPost.getId();

        em.flush();
        em.clear();

        PostDTO searchedPost = postService.getPost(savedPostId);
        Assertions.assertThat(searchedPost.getId()).isEqualTo(savedPost.getId());
    }

    @Test
    void removePost() {
        MemberDTO author = new MemberDTO("mason", "xxxx", "User");
        Member join = memberService.join(author, author.getRole());
        Post testPost1 = new Post("test post 1", "test content 1", join);
        PostDTO savedPost = postService.savePost(testPost1);

        em.flush();
        em.clear();
        List<PostDTO> allPosts = postService.getAllPosts();
        Assertions.assertThat(allPosts).hasSize(1);
        //postService.removePost(testPost1);
        allPosts = postService.getAllPosts();
        Assertions.assertThat(allPosts).hasSize(0);
    }

    @Test
    void getAllPosts() {
        MemberDTO author = new MemberDTO("mason", "xxxx", "User");
        Member join = memberService.join(author, author.getRole());
        Post testPost1 = new Post("test post 1", "test content 1", join);
        Post testPost2 = new Post("test post 2", "test content 2", join);
        Post testPost3 = new Post("test post 3", "test content 3", join);
        Post testPost4 = new Post("test post 4", "test content 4", join);
        postService.savePost(testPost1);
        postService.savePost(testPost2);
        postService.savePost(testPost3);
        postService.savePost(testPost4);

        em.flush();
        em.clear();

        List<PostDTO> allPosts = postService.getAllPosts();
        Assertions.assertThat(allPosts).hasSize(4);
    }

    @Test
    void getPostByName() {
        MemberDTO author = new MemberDTO("mason", "xxxx", "User");
        Member join = memberService.join(author, author.getRole());
        Post testPost1 = new Post("test post 1", "test content 1", join);
        Post testPost2 = new Post("test post 2", "test content 2", join);
        Post testPost3 = new Post("test post 3", "test content 3", join);
        Post testPost4 = new Post("test post 4", "test content 4", join);
        Post testPostDuplicate = new Post("test post 1", "test content 2", join);
        postService.savePost(testPost1);
        postService.savePost(testPost2);
        postService.savePost(testPost3);
        postService.savePost(testPost4);
        postService.savePost(testPostDuplicate);

        em.flush();
        em.clear();

        List<PostDTO> postByName = postService.getPostByName("test post 1");
        Assertions.assertThat(postByName).hasSize(2);
        List<PostDTO> postByName1 = postService.getPostByName("test post");
        Assertions.assertThat(postByName1).hasSize(5);
    }

    @Test
    void getPostByMember() {
        MemberDTO author1 = new MemberDTO("mason", "xxxx", "User");
        MemberDTO author2 = new MemberDTO("yunsik", "xxxxxx", "Admin");
        Member savedAuthor1 = memberService.join(author1, author1.getRole());
        Member savedAuthor2 = memberService.join(author2, author2.getRole());
        Post testPost1 = new Post("test post 1", "test content 1", savedAuthor1);
        Post testPost2 = new Post("test post 2", "test content 2", savedAuthor1);
        Post testPost3 = new Post("test post 3", "test content 3", savedAuthor1);
        Post testPost4 = new Post("test post 4", "test content 4", savedAuthor2);
        Post testPostDuplicate = new Post("test post 1", "test content 2", savedAuthor2);
        postService.savePost(testPost1);
        postService.savePost(testPost2);
        postService.savePost(testPost3);
        postService.savePost(testPost4);
        postService.savePost(testPostDuplicate);

        em.flush();
        em.clear();

        List<PostDTO> mason = postService.getPostByMember("mason");
        List<PostDTO> yunsik = postService.getPostByMember("yunsik");
        Assertions.assertThat(yunsik).hasSize(2);
        Assertions.assertThat(mason).hasSize(3);
    }

    @Test
    void getPostByContent() {
        MemberDTO author1 = new MemberDTO("mason", "xxxx", "User");
        MemberDTO author2 = new MemberDTO("yunsik", "xxxxxx", "Admin");
        Member savedAuthor1 = memberService.join(author1, author1.getRole());
        Member savedAuthor2 = memberService.join(author2, author2.getRole());
        Post testPost1 = new Post("test post 1", "test content 1", savedAuthor1);
        Post testPost2 = new Post("test post 2", "test content 2", savedAuthor1);
        Post testPost3 = new Post("test post 3", "test content 3", savedAuthor1);
        Post testPost4 = new Post("test post 4", "test content 4", savedAuthor2);
        Post testPostDuplicate = new Post("test post 1", "test content 2", savedAuthor2);
        postService.savePost(testPost1);
        postService.savePost(testPost2);
        postService.savePost(testPost3);
        postService.savePost(testPost4);
        postService.savePost(testPostDuplicate);

        em.flush();
        em.clear();

        List<PostDTO> testContent = postService.getPostByContent("test content");
        Assertions.assertThat(testContent).hasSize(5);
        List<PostDTO> postByContent = postService.getPostByContent("test content 2");
        Assertions.assertThat(postByContent).hasSize(2);
    }

    @Test
    void getPostByPostType() {
        // TO - DO
    }
}