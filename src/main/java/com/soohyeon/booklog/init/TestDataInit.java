package com.soohyeon.booklog.init;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.repository.BookRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final BookRepository bookRepository;

    @PostConstruct
    public void init() {
        if (bookRepository.findAll().isEmpty()) {
            BookDummyData.books().forEach(bookRepository::save);
        }
    }
}