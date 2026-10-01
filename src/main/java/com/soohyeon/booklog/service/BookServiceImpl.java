package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Author;
import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;
import com.soohyeon.booklog.repository.AuthorRepository;
import com.soohyeon.booklog.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본값은 읽기 전용으로
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    @Transactional
    public Book saveBook(Book book, Long memberId, String authorNames) {
        book.setMemberId(memberId);
        book.getAuthors().addAll(resolveAuthors(authorNames));
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
        if (keyword == null || keyword.isBlank()) return true;

        // 키워드 형식 단일화
        String searchKeyword = keyword.trim().toLowerCase();

        boolean matchTitle = book.getTitle() != null
                && book.getTitle().toLowerCase().contains(searchKeyword);

        // v3.0 : author를 별도의 객체로 분리 setAuthor 사용
        boolean matchAuthor = book.getAuthors().stream()
                .anyMatch(author -> author.getName() != null
                        && author.getName().toLowerCase().contains(searchKeyword));

        return matchTitle || matchAuthor;
    }


    /* update remove 할 때, 먼저 권한을 검증 */
    @Override
    @Transactional
    public void updateBook(Long bookId, Long memberId, Book updateParam, String authorNames) {
        Book book = bookRepository.findByIdAndMemberId(bookId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("본인 서재의 책만 수정할 수 있습니다."));

        // 트랜잭션 커밋 시 Dirty Checking으로 자동 update
        book.setTitle(updateParam.getTitle());
        book.setStatus(updateParam.getStatus());
        book.setRating(updateParam.getRating());
        book.setSummary(updateParam.getSummary());
        book.setMemo(updateParam.getMemo());

        // 기존 저자와의 연결을 끊고, 아예 새로운 저자와 연결 (내용물만)
        book.getAuthors().clear();
        book.getAuthors().addAll(resolveAuthors(authorNames));
    }

    /**
     * 콤마로 구분된 문자열을 Author 엔티티 집합으로 변환.
     * 이미 존재하는 저자명이면 재사용, 없으면 새로 생성(findOrCreate 패턴).
     */
    private Set<Author> resolveAuthors(String authorNames) {
        if (authorNames == null || authorNames.isBlank()) {
            return new HashSet<>();
        }

        return Arrays.stream(authorNames.split(","))
                .map(String::trim)
                .filter(name -> !name.isBlank())
                .map(name -> authorRepository.findByName(name)
                        .orElseGet(() -> authorRepository.save(new Author(name))))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteBook(Long bookId, Long memberId) {
        Book book = bookRepository.findByIdAndMemberId(bookId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("본인 서재의 책만 삭제할 수 있습니다."));

        bookRepository.delete(book);   // 기존 bookId를 파라미터로 받는 방식에서 Jpa 호환되는 파라미터로 변경
    }
}
