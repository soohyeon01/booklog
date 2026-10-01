package com.soohyeon.booklog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "book")
@EntityListeners(AuditingEntityListener.class)
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

    // Author 엔티티로 분리
//    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookStatus status;

    private Integer rating;

    @Column(length = 100)
    private String summary;

    @Column(length = 500)   // 기본 varchar(255)로는 500자 메모 잘림
    private String memo;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "uk_book_author",
                            columnNames = {"book_id", "author_id"}
                    )
            }
    )
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "book_tag",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"),
            uniqueConstraints = {   // 한개의 책에 같은 태그가 여러번 반복되지 않도록
            @UniqueConstraint(
                    name = "uk_book_tag",
                    columnNames = {"book_id", "tag_id"}
            )
    }
    )
    private Set<Tag> tags = new HashSet<>();

    // 기본 객체 생성자
    public Book() {
    }

    // id는 시스템에서 자동 발급하는 구조이므로 생성자에서 제외
    public Book(String title, BookStatus status, Integer rating, String summary, String memo) {
        this.title = title;
        this.status = status;
        this.rating = rating;
        this.summary = summary;
        this.memo = memo;
    }
}
