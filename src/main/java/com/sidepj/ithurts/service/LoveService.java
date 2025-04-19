package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Love;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoveService {

    Love findLoveById(Long loveId);

    List<Love> findLovesByPostId(Long postId);

    Page<Love> findLovesByPostId(Long postId, Pageable pageable);

    List<Love> findLovesByMemberId(Long postId);

    Page<Love> findLovesByMemberId(Long postId, Pageable pageable);

    void removeLove(Long postId, Long loveId);

    void addLove(Long postId, Love love);

    List<Love> getLoves(Long postId);

    Page<Love> getLovesByPostId(Long postId, Pageable pageable);
}
