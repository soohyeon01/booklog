package com.soohyeon.booklog.repository;

import com.soohyeon.booklog.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByLoginId(String loginId);
}
