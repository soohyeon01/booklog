package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Member;
import com.soohyeon.booklog.domain.MemberRole;
import com.soohyeon.booklog.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);   // 테스트용 낮은 강도
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 시 비밀번호는 평문이 아닌 BCrypt 해시로 저장되고 권한은 USER다")
    void signup_storesHashedPassword() {
        given(memberRepository.existsByLoginId("newuser")).willReturn(false);
        given(memberRepository.save(any(Member.class))).willAnswer(inv -> {
            Member m = inv.getArgument(0);
            m.setId(1L);
            return m;
        });

        memberService.signup("newuser", "plain-pass-1", "신규");

        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository).save(captor.capture());
        Member saved = captor.getValue();

        assertThat(saved.getPassword()).isNotEqualTo("plain-pass-1");
        assertThat(passwordEncoder.matches("plain-pass-1", saved.getPassword())).isTrue();
        assertThat(saved.getRole()).isEqualTo(MemberRole.USER);
    }

    @Test
    @DisplayName("이미 존재하는 아이디로 가입하면 예외가 발생하고 저장하지 않는다")
    void signup_throws_whenDuplicate() {
        given(memberRepository.existsByLoginId("user1")).willReturn(true);

        assertThatThrownBy(() -> memberService.signup("user1", "plain-pass-1", "중복"))
                .isInstanceOf(DuplicateLoginIdException.class);
        verify(memberRepository, never()).save(any());
    }

    @Test
    @DisplayName("올바른 비밀번호면 인증에 성공한다")
    void authenticate_success() {
        Member member = new Member("user1", passwordEncoder.encode("1234"), "김독서", MemberRole.USER);
        given(memberRepository.findByLoginId("user1")).willReturn(Optional.of(member));

        assertThat(memberService.authenticate("user1", "1234")).isPresent();
    }

    @Test
    @DisplayName("비밀번호가 틀리면 인증에 실패한다")
    void authenticate_fails_whenWrongPassword() {
        Member member = new Member("user1", passwordEncoder.encode("1234"), "김독서", MemberRole.USER);
        given(memberRepository.findByLoginId("user1")).willReturn(Optional.of(member));

        assertThat(memberService.authenticate("user1", "wrong")).isEmpty();
    }
}
