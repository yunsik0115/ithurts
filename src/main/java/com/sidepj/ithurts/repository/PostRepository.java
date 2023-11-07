package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 모든 Post를 반환
    Page<Post> findAll(Pageable pageable);

    // Post 이름을 기준으로 Post 검색
    @Query("select p from Post p where p.postName LIKE %:postName%")
    Page<Post> findPostsByPostName(@Param("postName") String postName, Pageable pageable);

    // Member의 이름을 기준으로 Post 검색
    @Query("select p from Post p join p.postMember where p.postMember.name = :name")
    Page<Post> findPostsByName(@Param("name") String name, Pageable pageable);

    // searchContent의 내용이 content의 일부인 글들을 검색함.
    @Query("select p from Post p where p.content LIKE %:searchContent%")
    Page<Post> findPostsByContent(@Param("searchContent") String searchContent, Pageable pageable);

}
