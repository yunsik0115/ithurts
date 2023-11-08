package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    // 1. Test Saving Member

    @Test
    @Transactional
    void saveTest(){

        // Given
        Member member = new Member("Yunsik", "Hello");

        em.flush();
        em.clear();
        // When
        Member savedMember = memberRepository.save(member);
        Long savedId = savedMember.getId();
        Optional<Member> searchedFromDB = memberRepository.findById(savedId);
        Member searchedMemberFromDB = searchedFromDB.get();

        // Then
        Assertions.assertThat(savedId).isEqualTo(searchedMemberFromDB.getId());

    }


}