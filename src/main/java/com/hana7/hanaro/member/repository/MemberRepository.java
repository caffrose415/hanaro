package com.hana7.hanaro.member.repository;

import java.util.List;
import java.util.Optional;

import com.hana7.hanaro.member.entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);

    boolean existsByEmail(String email);

    List<Member> findByDeleteAtIsNull();
    Optional<Member> findByIdAndDeleteAtIsNull(Long id);

}
