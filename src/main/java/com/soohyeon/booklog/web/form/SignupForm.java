package com.soohyeon.booklog.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupForm {

    @NotNull(message = "4~20자의 영문 대/소문자, 숫자만 입력 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "4~20자의 영문 대/소문자, 숫자만 입력 가능합니다.")
    private String loginId;

    @NotNull(message = "영문 대/소문자, 숫자, 특수문자를 사용하여 8~16자로 입력해주세요.")
    @Pattern(regexp = "^[\\x21-\\x7E]{8,16}$", message = "영문 대/소문자, 숫자, 특수문자를 사용하여 8~16자로 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호를 다시 한 번 입력해주세요.")
    private String passwordConfirm;

    @NotBlank(message = "이름을 입력하세요.")
    @Size(max = 50, message = "이름은 50자 이하로 입력해주세요.")
    private String name;

}
