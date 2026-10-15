package com.soohyeon.booklog.stats;

/**
 * 독서 통계, 독서 이력과 관련된 코드들은 서비스 로직에서 수정되지 않기 때문에
 * 레코드 타입으로 생성
 */

// repo 결과 조회
public record MonthlyReading(Integer year, Integer month, Long logCount, Long bookCount) {
}
