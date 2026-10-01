package com.soohyeon.booklog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reading_log")
@EntityListeners(AuditingEntityListener.class) // 자동 값 주입
@Getter
@Setter
public class ReadingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // 쌓이는 형태로 계속 기록 할 수 있음. book.memo와는 다른 역할
    @Column(nullable = false, length = 1000)
    private String content;

    // 월별 독서량 집계시 필요
    @CreatedDate
    @Column(name = "reading_date", nullable = false, updatable = false)
    private LocalDateTime readingDate;

    public ReadingLog() {
    }

    public ReadingLog(Book book, String content, LocalDate readingDate) {
        this.book = book;
        this.content = content;
    }
}
