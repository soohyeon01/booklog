package com.soohyeon.booklog.repository;

import com.soohyeon.booklog.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// 추후 상위 버전과의 호환성을 위해 인터페이스로 설계
public interface BookRepository extends JpaRepository<Book, Long> {

    // NOTE: v3.0부터 save(Book), findById(Long), findAll(), delete(Book)는 JpaRepository가 기본 제공
    
    List<Book> findAllByMemberId(Long memberId);

    Optional<Book> findByIdAndMemberId(Long id, Long memberId); // 타인 접근 제한용 메서드

}
