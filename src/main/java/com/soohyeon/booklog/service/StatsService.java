package com.soohyeon.booklog.service;

import com.soohyeon.booklog.domain.BookStatus;
import com.soohyeon.booklog.repository.BookRepository;
import com.soohyeon.booklog.repository.ReadingLogRepository;
import com.soohyeon.booklog.stats.MonthlyReading;
import com.soohyeon.booklog.stats.RatingCount;
import com.soohyeon.booklog.stats.ReadingStats;
import com.soohyeon.booklog.stats.StatusCount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private static final int MONTHS = 12;

    private final BookRepository bookRepository;
    private final ReadingLogRepository readingLogRepository;

    public ReadingStats getStats(Long memberId) {
        YearMonth current = YearMonth.now();
        YearMonth start = current.minusMonths(MONTHS - 1);

        // 1. 월별 DB 집계 결과 기록이 없으면 0으로 저장
        Map<YearMonth, MonthlyReading> byMonth = readingLogRepository
                .findMonthlyStats(memberId, start.atDay(1)).stream()
                .collect(Collectors.toMap(m -> YearMonth.of(m.year(), m.month()), Function.identity()));

        List<ReadingStats.MonthlyPoint> monthly = new ArrayList<>();
        for (YearMonth ym = start; !ym.isAfter(current); ym = ym.plusMonths(1)) {
            MonthlyReading m = byMonth.get(ym);
            monthly.add(new ReadingStats.MonthlyPoint(
                    ym.toString(), m == null ? 0 : m.logCount(), m == null ? 0 : m.bookCount()));
        }

        // 2. status 가 없는 경우도 0으로 저장
        Map<BookStatus, Long> statusMap = bookRepository.findStatusCount(memberId).stream()
                .collect(Collectors.toMap(StatusCount::status, StatusCount::count));
        List<ReadingStats.StatusPoint> statuses = Arrays.stream(BookStatus.values())
                .map(s -> new ReadingStats.StatusPoint(s, statusMap.getOrDefault(s, 0L)))
                .toList();

        // 3. 평점 분포
        Map<Integer, Long> ratingMap = bookRepository.findRatingCount(memberId).stream()
                .collect(Collectors.toMap(RatingCount::rating, RatingCount::count));
        List<ReadingStats.RatingPoint> ratings = IntStream.rangeClosed(1, 5)
                .mapToObj(r -> new ReadingStats.RatingPoint(r, ratingMap.getOrDefault(r, 0L)))
                .toList();

        long totalBooks = statuses.stream().mapToLong(ReadingStats.StatusPoint::count).sum();
        long thisMonthLogs = monthly.get(monthly.size() - 1).logCount();

        return new ReadingStats(
                totalBooks,
                readingLogRepository.countByBookMemberId(memberId),
                thisMonthLogs,
                bookRepository.findAverageRating(memberId),
                monthly, statuses, ratings,
                bookRepository.findTopTags(memberId, PageRequest.of(0, 5))
        );
    }




}
