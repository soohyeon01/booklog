package com.soohyeon.booklog.service;

/**
 * 중복 아이디 예외 검증 클래스
 */
public class DuplicateLoginIdException extends RuntimeException {

    public DuplicateLoginIdException(String loginId) {
        super("이미 사용 중인 아이디 입니다: " + loginId);
    }
}
