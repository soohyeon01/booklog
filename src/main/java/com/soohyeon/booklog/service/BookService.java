package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book saveBook(Book book, Long memberId);
    Optional<Book> findByBookId(Long id, Long memberId);
    List<Book> findBooks(Long memberId);
    List<Book> searchBooks(Long memberId, BookStatus status, String keyword, String sort);
    void updateBook(Long bookId, Long memberId, Book updateParam);
    void removeBook(Long bookId, Long memberId);
}
