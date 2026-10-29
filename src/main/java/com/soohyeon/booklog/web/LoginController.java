package com.soohyeon.booklog.web;

import com.soohyeon.booklog.domain.Member;
import com.soohyeon.booklog.repository.MemberRepository;
import com.soohyeon.booklog.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm() {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@RequestParam String loginId,
                        @RequestParam String password,
                        HttpServletRequest request) {

        // 찾을 수 없는 회원인 경우 error return
        Optional<Member> member = memberService.authenticate(loginId, password);
        if (member.isEmpty()) {
            return "redirect:/login?error";
        }

        HttpSession session = request.getSession();
        request.changeSessionId();      // 로그인 시 세션 ID 교체 - 세션 공격 방어
        session.setAttribute("loginMemberId", member.get().getId());
        return "redirect:/books";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/";
    }
}
