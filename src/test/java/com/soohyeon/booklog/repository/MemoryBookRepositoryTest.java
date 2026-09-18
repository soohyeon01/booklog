package com.soohyeon.booklog.repository;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryBookRepositoryTest {

    private final MemoryBookRepository repository = new MemoryBookRepository();

    @BeforeEach
    void init() {
        repository.clearStore(); // static Map을 쓰므로 테스트 간 상태 격리 필수
    }

    @Test
    @DisplayName("findAllByMemberId는 본인이 등록한 책만 반환한다")
    void findAllByMemberId_returnsOnlyOwnBooks() {
        // given
        saveWithMember("데미안", 1L);
        saveWithMember("이방인", 1L);
        saveWithMember("죄와 벌", 2L); // 다른 회원 책

        // when
        List<Book> member1Books = repository.findAllByMemberId(1L);

        // then
        assertThat(member1Books).hasSize(2);
        assertThat(member1Books).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("데미안", "이방인");
    }

    @Test
    @DisplayName("존재하는 책이어도 memberId가 다르면 조회되지 않는다")
    void findByIdAndMemberId_returnEmpty() {
        // given
        Book saved = saveWithMember("데미안", 1L);

        // when: 책은 실제 존재하지만, 2번 회원으로 조회 시도
        Optional<Book> result = repository.findByIdAndMemberId(saved.getId(), 2L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("본인 memberId로 조회하면 정상적으로 조회된다")
    void findByIdAndMemberId_returnsBook() {
        Book saved = saveWithMember("데미안", 1L);

        Optional<Book> result = repository.findByIdAndMemberId(saved.getId(), 1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("데미안");
    }

    @Test
    @DisplayName("update는 기존 책의 내용을 수정한다")
    void update() {
        // given
        Book saved = saveWithMember("데미안", 1L);

        Book updateParam = new Book("수정된 책", "수정된 저자",
                        BookStatus.DONE, 4, "수정된 요약", "수정된 메모");

        // when
        repository.update(saved.getId(), updateParam);

        // then
        Optional<Book> result = repository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("수정된 책");
        assertThat(result.get().getAuthor()).isEqualTo("수정된 저자");
        assertThat(result.get().getStatus()).isEqualTo(BookStatus.DONE);
    }

    @Test
    @DisplayName("delete는 해당 책을 삭제한다")
    void delete() {
        // given
        Book saved = saveWithMember("데미안", 1L);

        // when
        repository.delete(saved.getId());

        // then
        assertThat(repository.findById(saved.getId())).isEmpty();
    }

    private Book saveWithMember(String title, Long memberId) {
        Book book = new Book(title, "저자", BookStatus.WISH, 5, "요약", "메모");
        book.setMemberId(memberId);
        return repository.save(book);
    }
}