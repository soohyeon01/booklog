package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;
import com.soohyeon.booklog.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<Book> searchBooks(String keyword, BookStatus status) {

        List<Book> books = bookRepository.findAll();

        if (status != null) {
            books = books.stream()
                    .filter(book -> book.getStatus() == status)
                    .toList();
        }

        if (keyword != null && !keyword.isBlank()) {

            // 검색어 앞뒤 공백 제거
            keyword = keyword.trim();

            String searchKeyword = keyword.toLowerCase();

            books = books.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(searchKeyword)
                            || book.getAuthor().toLowerCase().contains(searchKeyword))
                    .toList();
        }
        return books;
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
