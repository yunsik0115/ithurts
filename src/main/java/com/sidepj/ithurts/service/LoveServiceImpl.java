package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Love;
import com.sidepj.ithurts.domain.Post;
import com.sidepj.ithurts.repository.LoveRepository;
import com.sidepj.ithurts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
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
    public void removeLove(Long postId, Long loveId) {
        // TO - DO Loves List Searched by postId must contain
        // the id which needs to be deleted.
        loveRepository.removeLoveById(loveId);
    }

    @Override
    public void addLove(Long postId, Love love) {
        loveRepository.save(love);
        Optional<Post> byId = postRepository.findById(postId);
        if(byId.isPresent()){
            Post post = byId.get();
            post.getLoves().add(love);
        }
        // TO - DO - Member Field Injection by what?
        loveRepository.save(love);
    }

    @Override
    public List<Love> getLoves(Long postId) {
        return loveRepository.findAll();
    }
}
