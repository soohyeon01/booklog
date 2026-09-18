package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;
import com.soohyeon.booklog.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

// 서비스 로직 격리 테스트
@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private final Long MY_MEMBER_ID = 1L;
    private final Long OTHER_MEMBER_ID = 2L;
    private final Long BOOK_ID = 100L;

    @Test
    @DisplayName("책을 등록하면 로그인되어있는 회원의 아이디가 자동 등록된다.")
    void saveBook_setMemberId() {

        //given
        Book book = new Book("데미안", "헤르만 헤세", BookStatus.READING, 5, "요약", "메모");

        // save()에 전달받은 Book 객체를 그대로 반환, DB를 거치지 않고 MOCK 사용
        // 메서드명 주의,,, .getArgument() / .getArguments()[]
        given(bookRepository.save(any(Book.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        Book saved = bookService.saveBook(book, MY_MEMBER_ID);

        //then
        assertThat(saved.getMemberId()).isEqualTo(MY_MEMBER_ID);
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("본인이 등록한 책은 수정 가능하다.")
    void updateBook_success_Owner() {

        //given
        Book myBook = creatBook(BOOK_ID, MY_MEMBER_ID);
        given(bookRepository.findByIdAndMemberId(BOOK_ID, MY_MEMBER_ID))
                .willReturn(Optional.of(myBook));

        Book updateParam = new Book("수정된 제목", "수정된 저자", BookStatus.DONE, 4, "s", "m");

        //when
        bookService.updateBook(BOOK_ID, MY_MEMBER_ID, updateParam);

        //then
        verify(bookRepository).update(BOOK_ID, updateParam);

    }

    @Test
    @DisplayName("본인이 등록한 책은 삭제 가능하다.")
    void deleteBook_success_Owner() {

        // given
        Book myBook = creatBook(BOOK_ID, MY_MEMBER_ID);

        given(bookRepository.findByIdAndMemberId(BOOK_ID, MY_MEMBER_ID))
                .willReturn(Optional.of(myBook));

        // when
        bookService.deleteBook(BOOK_ID, MY_MEMBER_ID);

        // then
        verify(bookRepository).delete(BOOK_ID);
    }

    @Test
    @DisplayName("다른 회원의 책은 수정할 수 없고, 수정을 시도하면 예외가 발생한다.")
    void updateBook_throw_whenNotOwner() {
        // given : (100L, 1L)이 호출되면 empty() return
        given(bookRepository.findByIdAndMemberId(BOOK_ID, MY_MEMBER_ID))
                .willReturn(Optional.empty());

        // 권한 없는 사용자가 수정 시도
        Book updateParam = new Book("해킹시도", "해커", BookStatus.DONE, 1, "s", "m");

        // when & then : (100L, 1L, updateParam) 호출해서 IllegalArgumentException 가 발생하는지 확인
        assertThatThrownBy(() -> bookService.updateBook(BOOK_ID, MY_MEMBER_ID, updateParam))
                .isInstanceOf(IllegalArgumentException.class);

        // 권한 검증에 실패했으므로 실제 update 로직은 절대 호출되면 안됨
        verify(bookRepository, never()).update(anyLong(), any(Book.class));
    }

    @Test
    @DisplayName("다른 회원의 책은 삭제할 수 없고, 삭제를 시도하면 예외가 발생한다.")
    void deleteBook_throw_whenNotOwner(){
        given(bookRepository.findByIdAndMemberId(BOOK_ID, MY_MEMBER_ID))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.deleteBook(BOOK_ID, MY_MEMBER_ID))
                .isInstanceOf(IllegalArgumentException.class);

        verify(bookRepository, never()).delete(anyLong());
    }

    @Test
    @DisplayName("본인 소유의 책을 조회할 수 있다.")
    void findByBookId_success_Owner() {
        // given
        Book myBook = creatBook(BOOK_ID, MY_MEMBER_ID);

        given(bookRepository.findByIdAndMemberId(BOOK_ID, MY_MEMBER_ID))
                .willReturn(Optional.of(myBook));

        // when
        Optional<Book> result = bookService.findByBookId(BOOK_ID, MY_MEMBER_ID);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(myBook);
    }

    @Test
    @DisplayName("findByBookId는 본인 소유가 아니면 empty 를 반환한다")
    void findByBookId_returnsEmpty_whenNotOwner() {
        given(bookRepository.findByIdAndMemberId(BOOK_ID, MY_MEMBER_ID))
                .willReturn(Optional.empty());

        Optional<Book> result = bookService.findByBookId(BOOK_ID, MY_MEMBER_ID);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("회원별로 자신의 책 목록만 조회한다.")
    void findBooks_returnsBooksByMemberId() {
        // given
        List<Book> myBooks = List.of(
                creatBook(1L, MY_MEMBER_ID),
                creatBook(2L, MY_MEMBER_ID)
        );

        given(bookRepository.findAllByMemberId(MY_MEMBER_ID))
                .willReturn(myBooks);

        // when
        List<Book> result = bookService.findBooks(MY_MEMBER_ID);

        // then
        assertThat(result).containsExactlyElementsOf(myBooks);
        verify(bookRepository).findAllByMemberId(MY_MEMBER_ID);
    }

    @Test
    @DisplayName("searchBooks는 status로 필터링할 수 있다.")
    void searchBooks_filtersByStatus() {
        // given
        List<Book> myBooks = List.of(
                createBookWithStatus("데미안", BookStatus.DONE, MY_MEMBER_ID),
                createBookWithStatus("이방인", BookStatus.WISH, MY_MEMBER_ID)
        );
        given(bookRepository.findAllByMemberId(MY_MEMBER_ID)).willReturn(myBooks);

        // when
        List<Book> result = bookService.searchBooks(MY_MEMBER_ID, BookStatus.DONE, null, null);

        // then : status.done 인 데미안만 필터링 되어야 함
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("searchBooks 는 제목 또는 저자에 키워드가 포함된 책을 필터링할 수 있다.")
    void searchBooks_filtersByKeyword() {
        List<Book> myBooks = List.of(
                createBookWithTitle("데미안", "헤르만 헤세", MY_MEMBER_ID),
                createBookWithTitle("이방인", "알베르 카뮈", MY_MEMBER_ID)
        );
        given(bookRepository.findAllByMemberId(MY_MEMBER_ID)).willReturn(myBooks);

        List<Book> result = bookService.searchBooks(MY_MEMBER_ID, null, "카뮈", null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("이방인");
    }

    private Book creatBook(Long id, Long memberId) {
        Book book = new Book("제목", "저자", BookStatus.WISH, 5, "요약", "메모");
        book.setId(id);
        book.setMemberId(memberId);
        return book;
    }

    private Book createBookWithStatus(String title, BookStatus status, Long memberId) {
        Book book = new Book(title, "저자", status, 5, "요약", "메모");
        book.setMemberId(memberId);
        return book;
    }

    private Book createBookWithTitle(String title, String author, Long memberId) {
        Book book = new Book(title, author, BookStatus.WISH, 5, "요약", "메모");
        book.setMemberId(memberId);
        return book;
    }
}


