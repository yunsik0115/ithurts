//package com.sidepj.ithurts.repository;
//
//import com.sidepj.ithurts.domain.Member;
//import com.sidepj.ithurts.domain.Post;
//import org.aspectj.lang.annotation.Before;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.context.event.annotation.BeforeTestExecution;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class PostRepositoryTest {
//
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private PostRepository postRepository;
//
//
//
//    @PersistenceContext
//    EntityManager em;
//
//    @BeforeAll
//    void before() {
//        Member member = new Member("Yunsik", "xxxxxx");
//        Post post1 = new Post("postTest1", "test content 1", member);
//        Post post2 = new Post("postTest2", "test content 2", member);
//        Post post3 = new Post("postTest3", "test content 3", member);
//        Post post4 = new Post("postTest4", "test content 4", member);
//        Post post5 = new Post("postTest5", "test content 5", member);
//        Post post6 = new Post("postTest6", "test content 6", member);
//        Post post7 = new Post("postTest7", "test content 7", member);
//        Post post8 = new Post("postTest8", "test content 8", member);
//        Post post9 = new Post("postTest9", "test content 9", member);
//        Post post10 = new Post("postTest10", "test content 10", member);
//
//        memberRepository.save(member);
//        postRepository.save(post1);
//        postRepository.save(post2);
//        postRepository.save(post3);
//        postRepository.save(post4);
//        postRepository.save(post5);
//        postRepository.save(post6);
//        postRepository.save(post7);
//        postRepository.save(post8);
//        postRepository.save(post9);
//        postRepository.save(post10);
//    }
//
//    @Test
//    void findAll() {
//        PageRequest pageRequest = PageRequest.of(0, 5);
//        Page<Post> retrievedPage = postRepository.findAll(pageRequest);
//
//        List<Post> page = retrievedPage.getContent();
//        Assertions.assertThat(page.size()).isEqualTo(5);
//        Assertions.assertThat(retrievedPage.getTotalElements()).isEqualTo(10);
//        Assertions.assertThat(retrievedPage.getTotalPages()).isEqualTo(2);
//        Assertions.assertThat(retrievedPage.getNumber()).isEqualTo(0); // Current Page Number
//        Assertions.assertThat(retrievedPage.isFirst()).isTrue();
//        Assertions.assertThat(retrievedPage.hasNext()).isTrue(); // because all page no should be 2
//    }
//
//    @Test
//    void findAllByPostName() {
//        PageRequest pageRequest = PageRequest.of(0, 5);
//        Page<Post> exactSameString = postRepository.findPostsByPostName("postTest1", pageRequest); // exact same string
//        Page<Post> notExactSameString = postRepository.findPostsByPostName("post", pageRequest); // not exact same string
//
//        List<Post> exactSameStringContent = exactSameString.getContent();
//        List<Post> notExactSameStringContent = notExactSameString.getContent();
//        Post searchedPost = exactSameStringContent.get(0);
//
//        Assertions.assertThat(searchedPost.getContent()).isEqualTo("test content 1");
//        Assertions.assertThat(notExactSameString.getTotalElements()).isEqualTo(10);
//    }
//
//    @Test
//    void findAllByMemberName() {
//        PageRequest pageRequest = PageRequest.of(0, 5);
//        Page<Post> searchedPageWithSameName = postRepository.findPostsByMemberName("Yunsik");
//        Page<Post> searchedPageWithNotSameName = postRepository.findPostsByMemberName("yun");
//    }
//
//    @Test
//    void findAllByContent() {
//    }
//}