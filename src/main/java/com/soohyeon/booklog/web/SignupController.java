package com.soohyeon.booklog.web;

import com.soohyeon.booklog.service.DuplicateLoginIdException;
import com.soohyeon.booklog.service.MemberService;
import com.soohyeon.booklog.web.form.SignupForm;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupForm", new SignupForm());
        return "signup/signupForm";
    }

    // BindingResult 는 항상 검증 대상 뒤에 위치해야 함
    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("signupForm") SignupForm form,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("password") && !bindingResult.hasFieldErrors("passwordConfirm")
            && !form.getPassword().equals(form.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "mismatch", "비밀번호가 일치하지 않습니다.");
        }
        if (bindingResult.hasErrors()) {
            return "signup/signupForm";
        }

        try {
            memberService.signup(form.getLoginId(), form.getPassword(), form.getName());
        } catch (DuplicateLoginIdException | DataIntegrityViolationException e) {
            // 서비스에서 아이디 중복을 확인, DB에 데이터를 저장하는 과정에서 UNIQUE 제약에 걸린 경우 모두 같은 메시지
            bindingResult.rejectValue("loginId", "duplicate", "이미 사용 중인 아이디입니다.");
            return "signup/signupForm";
        }

        redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해 주세요.");
        return "redirect:/login";

    }
}
