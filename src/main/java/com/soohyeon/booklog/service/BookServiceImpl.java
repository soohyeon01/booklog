package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Book;
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
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public List<Book> findBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void modifyBook(Long bookId, Book updateParam) {
        bookRepository.update(bookId, updateParam);
    }

    @Override
    public void removeBook(Long bookId) {
        bookRepository.delete(bookId);
    }
}
