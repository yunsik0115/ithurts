package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findMemberByName(String name);

}
