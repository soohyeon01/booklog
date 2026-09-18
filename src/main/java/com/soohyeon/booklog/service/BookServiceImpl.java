package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;
import com.soohyeon.booklog.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    // 롬복의 자동 생성자 주입 사용
    private final BookRepository bookRepository;

    @Override
    public Book saveBook(Book book, Long memberId) {
        book.setMemberId(memberId);
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> findByBookId(Long id, Long memberId) {
        return bookRepository.findByIdAndMemberId(id, memberId);
    }

    @Override
    public List<Book> findBooks(Long memberId) {
        return bookRepository.findAllByMemberId(memberId);
    }

    /**
     * @param status
     * @param keyword
     * @param sort
     * @return
     */
    @Override
    public List<Book> searchBooks(Long memberId, BookStatus status, String keyword, String sort) {
        Comparator<Book> comparator = getComparator(sort);

        // v2.0 - findAll() → findAllByMemberId()로만 바꿔주면 기존 필터/정렬 로직은 그대로 재사용
        return bookRepository.findAllByMemberId(memberId).stream()
                .filter(book -> filterByStatus(book, status))
                .filter(book -> filterByKeyword(book, keyword))
                .sorted(comparator)
                .toList();
    }

    /* 정렬 조건 메서드 */
    private Comparator<Book> getComparator(String sort) {
        if (sort == null) {
            sort = "id_desc";   // default: id 내림차순
        }

        return switch (sort) {
            case "id_asc" -> Comparator.comparing(Book::getId); // 아이디 오름차순 (오래된순)
            case "title_asc" -> Comparator.comparing(Book::getTitle, Comparator.nullsLast(String::compareTo)); // 제목 오름차순
            case "title_desc" -> Comparator.comparing(Book::getTitle, Comparator.nullsLast(String::compareTo)).reversed();   // 제목 내림차순
            case "rating_desc" -> Comparator.comparing(Book::getRating, Comparator.nullsLast(Integer::compareTo)).reversed(); // 평점 높은순
            case "rating_asc" -> Comparator.comparing(Book::getRating, Comparator.nullsLast(Integer::compareTo)); // 평점 낮은순
            default -> Comparator.comparing(Book::getId).reversed(); // 아이디 내림차순 (최신순)
        };
    }

    /* 상태 필터링 메서드 */
    private boolean filterByStatus(Book book, BookStatus status) {
        if (status == null) {
            return true; // status를 선택하지 않았다면 모든 객체 전체 통과
        }
        return book.getStatus() == status;
    }

    /* 키워드 검색 조건 메서드 분리(제목, 저자 검색) */
    private boolean filterByKeyword(Book book, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }
        // 키워드 형식 단일화
        String searchKeyword = keyword.trim().toLowerCase();

        boolean matchTitle = book.getTitle() != null
                && book.getTitle().toLowerCase().contains(searchKeyword);

        boolean matchAuthor = book.getAuthor() != null
                && book.getAuthor().toLowerCase().contains(searchKeyword);

        return matchTitle || matchAuthor;

    }


    /* update remove 할 때, 먼저 권한을 검증 */

    @Override
    public void updateBook(Long bookId, Long memberId, Book updateParam) {
        bookRepository.findByIdAndMemberId(bookId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("본인 서재의 책만 수정할 수 있습니다."));
        bookRepository.update(bookId, updateParam);
    }

    @Override
    public void removeBook(Long bookId, Long memberId) {
        bookRepository.findByIdAndMemberId(bookId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("본인 서재의 책만 삭제할 수 있습니다."));
        bookRepository.delete(bookId);
    }
}
