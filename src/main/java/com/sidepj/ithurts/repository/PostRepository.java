package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


}
