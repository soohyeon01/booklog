package com.soohyeon.booklog.repository;

import com.soohyeon.booklog.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId); // TODO 아래 메서드와 기능을 통합해야하지 않을까 싶음

    boolean existsByLoginId(String loginId);    // 로그인 시, 해당 메서드로 아이디 존재 유무 판별
}
