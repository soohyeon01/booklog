package com.soohyeon.booklog.init;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.Member;
import com.soohyeon.booklog.domain.MemberRole;
import com.soohyeon.booklog.repository.BookRepository;
import com.soohyeon.booklog.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        Member member1 = getOrCreateMember("user1", "1234", "김독서", MemberRole.USER);
        Member member2 = getOrCreateMember("user2", "1234", "이서재", MemberRole.USER);
        Member admin = getOrCreateMember("admin", "admin", "관리자", MemberRole.ADMIN);

        if (bookRepository.findAllByMemberId(member1.getId()).isEmpty()) {
            List<Book> books = BookDummyData.books();

            // 원할한 테스트를 위해 더미 데이터를 절반씩 나눠서 두 회원에게 배정
            int half = books.size() / 2;
            for (int i = 0; i < books.size(); i++) {
                Book book = books.get(i);
                book.setMemberId(i < half ? member1.getId() : member2.getId());
                bookRepository.save(book);
            }
        }
    }

    private Member getOrCreateMember(String loginId, String password, String name, MemberRole role) {
        return memberRepository.findByLoginId(loginId)
                .orElseGet(() -> memberRepository.save(new Member(loginId, password, name, role)));
    }
}