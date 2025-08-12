package com.hana7.hanaro.member.repository;

import com.hana7.hanaro.RepositoryTest;
import com.hana7.hanaro.member.entity.Auth;
import com.hana7.hanaro.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .email("testuser@gmail.com")
                .password("test123")
                .nickname("testuser")
                .auth(Auth.USERS)
                .build();
        testMember = memberRepository.save(member);
    }

    @Test
    @Order(1)
    void createMemberTest() {
        Member member = Member.builder()
            .email("tester@gmail.com")
            .password("test123")
            .nickname("tester")
            .auth(Auth.USERS)
            .build();

        Member savedMember = memberRepository.save(member);

        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getEmail()).isEqualTo("tester@gmail.com");
        assertThat(savedMember.getNickname()).isEqualTo("tester");
        assertThat(savedMember.getAuth()).isEqualTo(Auth.USERS);
    }

    @Test
    @Order(2)
    void findByEmailTest() {
        Member foundMember = memberRepository.findByEmail("testuser@gmail.com");

        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo(testMember.getEmail());
    }

    @Test
    @Order(3)
    void updateMemberTest() {
        Member memberToUpdate = memberRepository.findById(testMember.getId()).orElseThrow();
        Member updatedMember = Member.builder()
                .id(memberToUpdate.getId())
                .email(memberToUpdate.getEmail())
                .password(memberToUpdate.getPassword())
                .nickname("updatedUser")
                .auth(memberToUpdate.getAuth())
                .build();
        memberRepository.save(updatedMember);

        Member foundMember = memberRepository.findById(testMember.getId()).orElseThrow();
        assertThat(foundMember.getNickname()).isEqualTo("updatedUser");
    }

    @Test
    @Order(4)
    void deleteMemberTest() {
        memberRepository.deleteById(testMember.getId());

        assertThat(memberRepository.findById(testMember.getId())).isEmpty();
    }
}
