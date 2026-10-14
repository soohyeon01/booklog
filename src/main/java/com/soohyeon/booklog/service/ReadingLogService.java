package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.ReadingLog;
import com.soohyeon.booklog.repository.BookRepository;
import com.soohyeon.booklog.repository.ReadingLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadingLogService {

    private final BookRepository bookRepository;
    private final ReadingLogRepository readingLogRepository;

    // log를 찾아서 뿌려주는 메서드
    public List<ReadingLog> findLogs(Long bookId, Long memberId) {
        findOwnedBook(bookId, memberId);
        return readingLogRepository.findAllByBookIdOrderByReadingDateDescIdDesc(bookId);
    }

    // log를 등록하는 메서드
    @Transactional
    public void addLog(Long bookId, Long memberId, String content, LocalDate readingDate) {
        Book book = findOwnedBook(bookId, memberId);
        LocalDate date = (readingDate != null) ? readingDate : LocalDate.now(); // 사용자가 시간을 미입력할 경우
        readingLogRepository.save(new ReadingLog(book, content, date));
    }

    // log를 삭제하는 메서드
    @Transactional
    public void deleteLog(Long bookId, Long logId, Long memberId) {
        findOwnedBook(bookId, memberId);
        // logId가 내가 소유한 책에 속한 기록인지 한 번에 검증 (book과 log를 따로 검증하면 내 책과 다른 회원의 로그 조합이 가능해짐)
        ReadingLog log = readingLogRepository.findByIdAndBookId(logId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 독서 기록입니다."));
        readingLogRepository.delete(log);
    }

    // 소유권이 있는 책을 찾는 서브 메서드
    private Book findOwnedBook(Long bookId, Long memberId) {
        return bookRepository.findByIdAndMemberId(bookId, memberId)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 도서입니다."));
    }
}
