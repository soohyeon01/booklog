package com.soohyeon.booklog.repository;

import com.soohyeon.booklog.domain.ReadingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadingLogRepository extends JpaRepository<ReadingLog, Long> {

    List<ReadingLog> findAllByBookId(Long bookId);
}
