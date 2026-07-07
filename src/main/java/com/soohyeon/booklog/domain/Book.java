package com.soohyeon.booklog.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Book {

    private Long id;        // 자동 발급 고유 번호
    private String title;   // 필수 입력값
    private String author;
    private BookStatus status;
    private Integer rating;
    private String summary;
    private String memo;

    // 기본 객체 생성자
    public Book() {
    }

    // id는 시스템에서 자동 발급하는 구조이므로 생성자에서 제외
    public Book(String title, String author, BookStatus status, Integer rating, String summary, String memo) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.rating = rating;
        this.summary = summary;
        this.memo = memo;
    }
}
