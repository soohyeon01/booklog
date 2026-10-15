package com.soohyeon.booklog.stats;

import com.soohyeon.booklog.domain.BookStatus;

import java.util.List;

public record ReadingStats(long totalBooks, long totalLogs, long thisMonthLogs, double averageRating,
                           List<MonthlyPoint> monthly, List<StatusPoint> statuses,
                           List<RatingPoint> ratings, List<NameCount> topTags) {

    // 화면에 전달할 통계 데이터
    public record MonthlyPoint(String month, long logCount, long bookCount) {}
    public record StatusPoint(BookStatus status, long count) {}
    public record RatingPoint(int rating, long count) {}

    // 차트에 사용할 데이터 추출
    public List<String> monthLabels()    { return monthly.stream().map(MonthlyPoint::month).toList(); }
    public List<Long> monthLogCounts()   { return monthly.stream().map(MonthlyPoint::logCount).toList(); }
    public List<Long> monthBookCounts()  { return monthly.stream().map(MonthlyPoint::bookCount).toList(); }
    public List<String> statusLabels()   { return statuses.stream().map(s -> s.status().getDescription()).toList(); }
    public List<Long> statusCounts()     { return statuses.stream().map(StatusPoint::count).toList(); }
    public List<Long> ratingCounts()     { return ratings.stream().map(RatingPoint::count).toList(); }

}
