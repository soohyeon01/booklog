package com.soohyeon.booklog.web;

import com.soohyeon.booklog.domain.Member;
import com.soohyeon.booklog.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberRepository memberRepository;

    @GetMapping("/login")
    public String loginForm() {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@RequestParam String loginId,
                        @RequestParam String password,
                        HttpServletRequest request) {

        Member member = memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);

        if (member == null) {
            return "redirect:/login?error";
        }

        // 추후 설계와 성능을 고려하면 loginId가 아니라 Id 로 회원을 식별하는 것이 유리함
        //  DB에서도 Id를 PK로 사용함
        request.getSession().setAttribute("loginMemberId", member.getId());
        return "redirect:/books";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/";
    }
}
