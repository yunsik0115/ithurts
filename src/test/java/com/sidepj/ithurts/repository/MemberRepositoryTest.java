package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    void saveMember(){
        Member member = new Member("yunsik", "1234");
        Long memberId = memberRepository.save(member).getId();
        em.flush();
        em.clear();

        Optional<Member> findMember = memberRepository.findById(memberId);
        Member findedMember = findMember.get();
        Assertions.assertThat(findedMember.getId()).isEqualTo(memberId);
    }

    @Test
    @Transactional
    void findMember() {
        Member member = new Member("yunsik", "1234");
        Long memberId = memberRepository.save(member).getId();
        Optional<Member> memberById = memberRepository.findById(memberId);
        Member member1 = memberById.get();
        Member member2 = memberRepository.findMemberByName("yunsik");

        Assertions.assertThat(member1).isEqualTo(member2);
    }

}