package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.BookStatus;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book saveBook(Book book);
    Optional<Book> findByBookId(Long id);
    List<Book> findBooks();
    List<Book> searchBooks(String keyword, BookStatus status);
    void updateBook(Long bookId, Book updateParam);
    void removeBook(Long bookId);
}
