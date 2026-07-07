package com.soohyeon.booklog.repository;

import com.soohyeon.booklog.domain.Book;

import java.util.List;
import java.util.Optional;

// 추후 상위 버전과의 호환성을 위해 인터페이스로 설계
public interface BookRepository {

    Book save(Book book);                       // 저장
    Optional<Book> findById(Long id);           // 단건 조회
    List<Book> findAll();                       // 전체 조회
    void update(Long bookId, Book updateParam); // 수정
    void delete(Long bookId);                              // 삭제
}
