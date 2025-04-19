package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByName(String name);

    Page<Member> findAll(Pageable pageable);
    Page<Member> findMemberByNameLike(String name, Pageable pageable);

}
