package com.hana7.hanaro.member.service;

import com.hana7.hanaro.member.dto.MemberAdminDTO;
import com.hana7.hanaro.member.dto.SignupRequestDTO;
import com.hana7.hanaro.member.entity.Auth;
import com.hana7.hanaro.member.entity.Member;
import com.hana7.hanaro.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(SignupRequestDTO signupRequestDTO) {
        if (memberRepository.existsByEmail(signupRequestDTO.email())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        Member member = Member.builder()
                .email(signupRequestDTO.email())
                .password(passwordEncoder.encode(signupRequestDTO.password()))
                .nickname(signupRequestDTO.nickname())
                .auth(Auth.USERS)
                .build();

        memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberAdminDTO> list(boolean includeDeleted) {
        List<Member> members = includeDeleted
            ? memberRepository.findAll()
            : memberRepository.findByDeleteAtIsNull();
        return members.stream().map(MemberAdminDTO::from).toList();
    }

    @Override
    @Transactional
    public void softDelete(Long memberId) {
        Member m = memberRepository.findByIdAndDeleteAtIsNull(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원이 없거나 이미 삭제되었습니다."));
        m.setDeleteAt(LocalDateTime.now());
        memberRepository.flush();
    }
}
