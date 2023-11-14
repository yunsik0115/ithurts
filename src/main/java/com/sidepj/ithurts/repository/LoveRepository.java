package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Love;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoveRepository extends JpaRepository<Love, Long> {

    List<Love> findByPostId(Long postId);
    void removeLoveById(Long loveId);
}
