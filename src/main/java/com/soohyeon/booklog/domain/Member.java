package com.soohyeon.booklog.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * id, loginId, password, name
 */
public class Member {

    private Long id;
    private String loginId;
    private String password;
    private String name;
    private MemberRole role;    // v2.0 추가

    public Member() {
    }

    public Member(String loginId, String password, String name, MemberRole role) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.role = role;
    }
}
