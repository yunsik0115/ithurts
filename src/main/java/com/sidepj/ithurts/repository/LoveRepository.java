package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Love;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoveRepository extends JpaRepository<Love, Long> {

    Optional<Love> findByPostId(Long postId);
    void removeLoveById(Long loveId);

    Optional<Love> findById(Long id);
}
