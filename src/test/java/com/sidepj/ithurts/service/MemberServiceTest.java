package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.service.dto.MemberDTO;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @PersistenceContext
    EntityManager em;

    @Transactional
    @AfterEach
    public void execute() {
        em.flush();
        em.createNativeQuery("TRUNCATE TABLE " + "members").executeUpdate();
    }


    @Test
    void join() {
        MemberDTO memberDTO1 = new MemberDTO("Yunsik", "xxxx", "User");
        MemberDTO memberDTO2 = new MemberDTO("Mason", "abcedfg", "Admin");
        MemberDTO memberDTO3 = new MemberDTO("", "jackson", "User");
        Member yunsikJoin = memberService.join(memberDTO1, memberDTO1.getRole());
        Member masonJoin = memberService.join(memberDTO2, memberDTO2.getRole());

        Long yunsikId = yunsikJoin.getId();
        Long masonId = masonJoin.getId();

        em.flush();
        em.clear();

        MemberDTO yunsik = memberService.getMember("Yunsik");
        MemberDTO mason = memberService.getMember("Mason");


        org.assertj.core.api.Assertions.assertThat(yunsik.getId()).isEqualTo(yunsikId);
        org.assertj.core.api.Assertions.assertThat(mason.getId()).isEqualTo(masonId);
        Assertions.assertThrows(IllegalArgumentException.class ,() -> memberService.join(memberDTO3, memberDTO3.getRole()));
    }


    @Test
    void getMembers() {
        MemberDTO memberDTO1 = new MemberDTO("Yunsik", "xxxx", "User");
        MemberDTO memberDTO2 = new MemberDTO("Mason", "abcedfg", "Admin");
        Member yunsikJoin = memberService.join(memberDTO1, memberDTO1.getRole());
        Member masonJoin = memberService.join(memberDTO2, memberDTO2.getRole());
        List<MemberDTO> members = memberService.getMembers();
        org.assertj.core.api.Assertions.assertThat(members).hasSize(2);
    }

    @Test
    void getMember() {
        MemberDTO memberDTO1 = new MemberDTO("Yunsik", "xxxx", "User");
        Member join = memberService.join(memberDTO1, memberDTO1.getRole());

        em.flush();
        em.clear();

        MemberDTO yunsik = memberService.getMember("Yunsik");
        org.assertj.core.api.Assertions.assertThat(yunsik.getId()).isEqualTo(join.getId());

    }

    @Test
    void getRole() {
        MemberDTO memberDTO1 = new MemberDTO("Yunsik", "xxxx", "User");
        Member join = memberService.join(memberDTO1, memberDTO1.getRole());

        em.flush();
        em.clear();

        String yunsikRole = memberService.getRole("Yunsik");
        org.assertj.core.api.Assertions.assertThat(yunsikRole).isEqualTo(join.getRole());
    }


}