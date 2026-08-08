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
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> findByBookId(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findBooks() {
        return bookRepository.findAll();
    }

    /**
     * 키워드와 상태로 책을 검색하는 기능
     * @param keyword
     * @param status
     * @return
     */
    @Override
    public List<Book> searchBooks(BookStatus status, String keyword, String sort) {

        /*TODO: 의미 있는 로직끼리 묶어서 별도의 메서드로 분리하는게 낫지 않을까 싶음*/
        
        // 전체 목록 조회
        List<Book> books = bookRepository.findAll();

        // 상태 필터링
        if (status != null) {
            books = books.stream()
                    .filter(book -> book.getStatus() == status)
                    .toList();
        }
        
        // 키워드 검색
        if (keyword != null && !keyword.isBlank()) {
            // 검색어 앞뒤 공백 제거
            keyword = keyword.trim();
            // 대문자로 들어오는 검색어 소문자로 통일
            String searchKeyword = keyword.toLowerCase();
            books = books.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(searchKeyword)
                            || book.getAuthor().toLowerCase().contains(searchKeyword))
                    .toList();
        }
        
        // 정렬 조건 처리
        Comparator<Book> comparator = getComparator(sort);

        return books.stream()
                .sorted(comparator)
                .toList();
    }

    private Comparator<Book> getComparator(String sort) {
        if (sort == null) {
            sort = "id_desc";   // default: id 내림차순
        }

        return switch (sort) {
            case "id_asc" -> Comparator.comparing(Book::getId); // 아이디 오름차순 (오래된순)
            case "title" -> Comparator.comparing(Book::getTitle, Comparator.nullsLast(String::compareTo)); // 제목 오름차순
            // TODO: 제목 내림차순 개발
            case "rating_desc" -> Comparator.comparing(Book::getRating).reversed(); // 평점 내림차순
            case "rating_asc" -> Comparator.comparing(Book::getRating); // 평점 오름차순
            default -> Comparator.comparing(Book::getId).reversed(); // 아이디 내림차순 (최신순)
        };
    }

    @Override
    public void updateBook(Long bookId, Book updateParam) {
        bookRepository.update(bookId, updateParam);
    }

    @Override
    public void removeBook(Long bookId) {
        bookRepository.delete(bookId);
    }
}
