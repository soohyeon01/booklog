package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Member;
import com.soohyeon.booklog.domain.MemberRole;
import com.soohyeon.booklog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signup(String loginId, String rawPassword, String name) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new DuplicateLoginIdException(loginId);
        }

        // role은 user로 항상 고정
        Member member = new Member(loginId, passwordEncoder.encode(rawPassword), name, MemberRole.USER);
        return memberRepository.save(member).getId();
    }

    public Optional<Member> authenticate(String loginId, String rawPassword) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> passwordEncoder.matches(rawPassword, m.getPassword()));
    }
}
