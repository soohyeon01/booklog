package com.soohyeon.booklog.repository;

import com.soohyeon.booklog.domain.ReadingLog;
import com.soohyeon.booklog.stats.MonthlyReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReadingLogRepository extends JpaRepository<ReadingLog, Long> {

    List<ReadingLog> findAllByBookIdOrderByReadingDateDescIdDesc(Long bookId);   // 등록한 시간이 같다면 id 내림차순으로 정렬

    void deleteAllByBookId(Long bookId);

    /**
     * v3.3 독서통계 코드 추가
     */
    @Query("""
            select new com.soohyeon.booklog.stats.MonthlyReading(
                year(l.readingDate), month(l.readingDate), count(l), count(distinct b.id))
            from ReadingLog l join l.book b
            where b.memberId = :memberId and l.readingDate >= :from
            group by year(l.readingDate), month(l.readingDate)
            """)
    List<MonthlyReading> findMonthlyStats(@Param("memberId") Long memberId, @Param("from") LocalDate from);

    long countByBookMemberId(Long memberId);    // 회원 당 전체 기록 수

    // 소유권이 있는 책만 삭제할 수 있음
    // readingLog 객체는 memberId를 직접 명시하고 있지 않으므로, readingLog -> book -> member의 루트로 소유권을 확인해야함
    Optional<ReadingLog> findByIdAndBookMemberId(Long id, Long memberId);

    // 이력 조회: book을 fetch join해서 목록 N+1 방지, 페이징에는 countQuery 별도 지정
    @Query(value = """
            select l from ReadingLog l join fetch l.book b
            where b.memberId = :memberId
                and (:from is null or l.readingDate >= :from)
                and (:to is null or l.readingDate < :to)
            order by l.readingDate desc, l.id desc
            """,
            countQuery = """
                    select count(l) from ReadingLog l join l.book b
                    where b.memberId = :memberId
                      and (:from is null or l.readingDate >= :from)
                      and (:to is null or l.readingDate < :to)
                    """)
    Page<ReadingLog> findHistory(@Param("memberId") Long memberId,
                                 @Param("from") LocalDate from,
                                 @Param("to") LocalDate to,
                                 Pageable pageable);
}
