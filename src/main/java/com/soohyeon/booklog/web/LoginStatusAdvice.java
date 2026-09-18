package com.soohyeon.booklog.web;

import com.soohyeon.booklog.domain.Member;
import com.soohyeon.booklog.repository.MemberRepository;
import com.soohyeon.booklog.web.argumentresolver.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Spring Security 를 쓰지 않고 세션 로그인 방식을 사용하고 있으므로, 컨트롤러에서 model에 로그인 여부를 담아 넘기는 방식 선택
 * 
 * NOTE: 코드의 활용성을 생각해서 아래 두 메서드를 합칠지 그냥 둘지 고려
 */
@ControllerAdvice
@RequiredArgsConstructor
public class LoginStatusAdvice {

    private final MemberRepository memberRepository;

    // 로그인 여부 검사 후 뷰에 전달
    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(@LoginMember Long memberId) {
        return memberId != null;
    }

    // 뷰 렌더링 시, 회원의 이름을 표시하기 위해 Model에 회원 정보를 넣어줌
    @ModelAttribute("loginMember")
    public Member loginMember(@LoginMember Long memberId) {
        if (memberId == null) {
            return null;
        }
        return memberRepository.findById(memberId).orElse(null);
    }
    
    // 버전 관리
    @Value("${booklog.version}")
    private String booklogVersion;

    @ModelAttribute("booklogVersion")
    public String booklogVersion() {
        return booklogVersion;
    }
}
