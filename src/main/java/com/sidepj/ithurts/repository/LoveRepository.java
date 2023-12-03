package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Love;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoveRepository extends JpaRepository<Love, Long> {

    @Query("select l from Love l where l.love_member = :memberId")
    Optional<List<Love>> findByMemberId(Long memberId);

    @Query("select l from Love l where l.post = :postId")
    Optional<List<Love>> findByPostId(Long postId);

    @Query("delete from Love l where l.id =:loveId")
    void removeLove(Long loveId);

    Optional<Love> findById(Long id);
}
