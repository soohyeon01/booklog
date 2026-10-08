package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Author;
import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;
import com.soohyeon.booklog.repository.AuthorRepository;
import com.soohyeon.booklog.repository.BookRepository;
import com.soohyeon.booklog.repository.ReadingLogRepository;
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
    private final BookRelationResolver relationResolver;      // AuthorRepository 직접 주입 대신 교체
    private final ReadingLogRepository readingLogRepository;  // removeBook에서 사용


    // TODO: 시용자가 태그를 등록한 순서대로 화면에 노출되도록 수정. 현재는 목록에서 새로고침할 때마다 태그의 순서가 랜덤으로 배정됨
    @Override
    @Transactional
    public Book saveBook(Book book, Long memberId, String authorNames, String tagNames) {
        book.setMemberId(memberId);
        book.getAuthors().addAll(relationResolver.resolveAuthors(authorNames)); // resolver에서 가져다쓰는 방식으로 변경
        book.getTags().addAll(relationResolver.resolveTags(tagNames));
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

    /* 키워드 검색 조건 메서드 분리 */
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

        boolean matchTag = book.getTags().stream()
                .anyMatch(tag -> tag.getName() != null && tag.getName().toLowerCase().contains(searchKeyword));


        return matchTitle || matchAuthor || matchTag;
    }


    /* update remove 할 때, 먼저 권한을 검증 */
    @Override
    @Transactional
    public void updateBook(Long bookId, Long memberId, Book updateParam, String authorNames, String tagNames) {
        Book book = bookRepository.findByIdAndMemberId(bookId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("본인 서재의 책만 수정할 수 있습니다."));

        // 트랜잭션 커밋 시 Dirty Checking으로 자동 update
        book.setTitle(updateParam.getTitle());
        book.setStatus(updateParam.getStatus());
        book.setRating(updateParam.getRating());
        book.setSummary(updateParam.getSummary());
        book.setMemo(updateParam.getMemo());

        // resolver 를 별도로 분리하고 tag 필드 추가
        book.getAuthors().clear();
        book.getAuthors().addAll(relationResolver.resolveAuthors(authorNames));
        book.getTags().clear();
        book.getTags().addAll(relationResolver.resolveTags(tagNames));
    }

    /* 기존 resolveAuthors 메서드 제거 */

    @Override
    @Transactional
    public void deleteBook(Long bookId, Long memberId) {
        Book book = bookRepository.findByIdAndMemberId(bookId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("본인 서재의 책만 삭제할 수 있습니다."));

        readingLogRepository.deleteAllByBookId(bookId);   // reading_log FK 때문에 먼저 삭제
        bookRepository.delete(book);
    }
}
