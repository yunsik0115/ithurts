package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Love;

import java.util.List;

public interface LoveService {

    Love findLoveById(Long loveId);

    List<Love> findLovesByPostId(Long postId);

    List<Love> findLovesByMemberId(Long postId);

    void removeLove(Long postId, Long loveId);

    void addLove(Long postId, Love love);

    List<Love> getLoves(Long postId);
}
