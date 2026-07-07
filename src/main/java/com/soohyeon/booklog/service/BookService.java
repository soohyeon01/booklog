package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Long registerBook(Book book);
    Optional<Book> findBookById(Long id);
    List<Book> findBooks();
    void modifyBook(Long bookId, Book updateParam);
    void removeBook(Long bookId);
}
