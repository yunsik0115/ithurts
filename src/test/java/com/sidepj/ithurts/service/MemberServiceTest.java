package com.sidepj.ithurts.service;

import com.sidepj.ithurts.service.dto.MemberControllerDTO;
import com.sidepj.ithurts.service.dto.MemberJoinDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        MemberJoinDTO memberJoinDTO1 = new MemberJoinDTO("Yunsik", "xxxx", "User");
        MemberJoinDTO memberJoinDTO2 = new MemberJoinDTO("Mason", "abcedfg", "Admin");
        MemberJoinDTO memberJoinDTO3 = new MemberJoinDTO("", "jackson", "User");
        MemberControllerDTO yunsikJoin = memberService.join(memberJoinDTO1, memberJoinDTO1.getRole());
        MemberControllerDTO masonJoin = memberService.join(memberJoinDTO2, memberJoinDTO2.getRole());

        Long yunsikId = yunsikJoin.getId();
        Long masonId = masonJoin.getId();

        em.flush();
        em.clear();

        MemberControllerDTO yunsik = memberService.getMemberByName("Yunsik");
        MemberControllerDTO mason = memberService.getMemberByName("Mason");


        org.assertj.core.api.Assertions.assertThat(yunsik.getId()).isEqualTo(yunsikId);
        org.assertj.core.api.Assertions.assertThat(mason.getId()).isEqualTo(masonId);
        Assertions.assertThrows(IllegalArgumentException.class ,() -> memberService.join(memberJoinDTO3, memberJoinDTO3.getRole()));
    }


    @Test
    void getMembers() {
        MemberJoinDTO memberJoinDTO1 = new MemberJoinDTO("Yunsik", "xxxx", "User");
        MemberJoinDTO memberJoinDTO2 = new MemberJoinDTO("Mason", "abcedfg", "Admin");
        MemberControllerDTO yunsikJoin = memberService.join(memberJoinDTO1, memberJoinDTO1.getRole());
        MemberControllerDTO masonJoin = memberService.join(memberJoinDTO2, memberJoinDTO2.getRole());
        List<MemberControllerDTO> members = memberService.getMembers();
        org.assertj.core.api.Assertions.assertThat(members).hasSize(2);
    }

    @Test
    void getMember() {
        MemberJoinDTO memberJoinDTO1 = new MemberJoinDTO("Yunsik", "xxxx", "User");
        MemberControllerDTO join = memberService.join(memberJoinDTO1, memberJoinDTO1.getRole());

        em.flush();
        em.clear();

        MemberControllerDTO yunsik = memberService.getMemberByName("Yunsik");
        org.assertj.core.api.Assertions.assertThat(yunsik.getId()).isEqualTo(join.getId());

    }

    @Test
    void getRole() {
        MemberJoinDTO memberJoinDTO1 = new MemberJoinDTO("Yunsik", "xxxx", "User");
        MemberControllerDTO join = memberService.join(memberJoinDTO1, memberJoinDTO1.getRole());

        em.flush();
        em.clear();

        String yunsikRole = memberService.getRole("Yunsik");
        org.assertj.core.api.Assertions.assertThat(yunsikRole).isEqualTo(join.getRole());
    }


}