package com.soohyeon.booklog.repository;

import com.soohyeon.booklog.domain.ReadingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReadingLogRepository extends JpaRepository<ReadingLog, Long> {

    List<ReadingLog> findAllByBookIdOrderByReadingDateDescIdDesc(Long bookId);   // 등록한 시간이 같다면 id 내림차순으로 정렬
    Optional<ReadingLog> findByIdAndBookId(Long id, Long bookId);
    void deleteAllByBookId(Long bookId);
}
