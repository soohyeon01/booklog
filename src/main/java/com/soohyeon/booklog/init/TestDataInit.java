package com.soohyeon.booklog.init;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.Member;
import com.soohyeon.booklog.repository.BookRepository;
import com.soohyeon.booklog.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        Member testMember = memberRepository.findByLoginId("test")
                .orElseGet(() -> memberRepository.save(new Member("test", "1234", "테스트유저")));

        if (bookRepository.findAllByMemberId(testMember.getId()).isEmpty()) {
            BookDummyData.books().forEach(book -> {
                book.setMemberId(testMember.getId());
                bookRepository.save(book);
            });
        }
    }
}