package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Love;
import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.repository.LoveRepository;
import com.sidepj.ithurts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoveServiceImpl implements LoveService{

    private final PostRepository postRepository;
    private final LoveRepository loveRepository;

    @Override
    public Love findLoveById(Long loveId) {
        Optional<Love> findLove = loveRepository.findById(loveId);
        if(findLove.isEmpty()){
            throw new IllegalArgumentException("좋아요를 찾을 수 없습니다");
        }
        return findLove.get();
    }

    @Override
    public List<Love> findLovesByPostId(Long postId) {
        Optional<List<Love>> findLovesByPostId = loveRepository.findByPostId(postId);
        if(findLovesByPostId.isEmpty()){
            throw new IllegalArgumentException("해당 글/댓글에 좋아요가 없습니다");
        }
        List<Love> loves = findLovesByPostId.get();
        return loves;
    }

    @Override
    public List<Love> findLovesByMemberId(Long memberId) {
        Optional<List<Love>> findLovesByMemberId = loveRepository.findByMemberId(memberId);
        if(findLovesByMemberId.isEmpty()){
            throw new IllegalArgumentException("아직 좋아요가 없습니다");
        }
        return findLovesByMemberId.get();
    }

    @Override
    public void addLove(Long postId, Love love) {
        Optional<Post> byId = postRepository.findById(postId);
        if(byId.isPresent()){
            Post post = byId.get();
            love.setPost(post);
            post.getLoves().add(love);
        }
        // TO - DO - Member Field Injection by what?
        loveRepository.save(love);
    }

    @Override
    public void removeLove(Long postId, Long loveId) {
        // TO - DO Loves List Searched by postId must contain
        // the id which needs to be deleted.
        Love loveById = findLoveById(loveId);
        loveRepository.removeLove(loveById.getId());
    }

    @Override
    public List<Love> getLoves(Long postId) {
        return loveRepository.findAll();
    }

    @Override
    public Page<Love> findLovesByPostId(Long postId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Love> findLovesByMemberId(Long postId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Love> getLovesByPostId(Long postId, Pageable pageable) {
        return null;
    }
}
