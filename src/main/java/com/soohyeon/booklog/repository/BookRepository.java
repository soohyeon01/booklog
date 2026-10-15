package com.soohyeon.booklog.repository;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.stats.NameCount;
import com.soohyeon.booklog.stats.RatingCount;
import com.soohyeon.booklog.stats.StatusCount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// 추후 상위 버전과의 호환성을 위해 인터페이스로 설계
public interface BookRepository extends JpaRepository<Book, Long> {

    // NOTE: v3.0부터 save(Book), findById(Long), findAll(), delete(Book)는 JpaRepository가 기본 제공

    List<Book> findAllByMemberId(Long memberId);

    Optional<Book> findByIdAndMemberId(Long id, Long memberId); // 타인 접근 제한용 메서드

    /**
     * v3.3 독서통계 코드 추가
     */
    @Query("""
            select new com.soohyeon.booklog.stats.StatusCount(b.status, count(b))
            from Book b where b.memberId = :memberId group by b.status
            """)
    List<StatusCount> findStatusCount(@Param("memberId") Long memberId);

    @Query("""
            select new com.soohyeon.booklog.stats.RatingCount(b.rating, count(b))
            from Book b where b.memberId = :memberId and b.rating is not null group by b.rating
            """)
    List<RatingCount> findRatingCount(@Param("memberId") Long memberId);

    @Query("select avg(b.rating) from Book b where b.memberId = :memberId and b.rating is not null")
    Double findAverageRating(@Param("memberId") Long memberId);

    @Query("""
        select new com.soohyeon.booklog.stats.NameCount(t.name, count(b))
        from Book b join b.tags t
        where b.memberId = :memberId
        group by t.name
        order by count(b) desc, t.name asc
        """)
    List<NameCount> findTopTags(@Param("memberId") Long memberId, Pageable pageable);

}
