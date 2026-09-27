package com.soohyeon.booklog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book")

@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // 자동 발급 고유 번호

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 50)
    private String title;   // 필수 입력값

    @Column(nullable = false, length = 50)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookStatus status;

    private Integer rating;

    @Column(length = 100)
    private String summary;

    @Column(length = 500)   // 기본 varchar(255)로는 500자 메모 잘림
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
