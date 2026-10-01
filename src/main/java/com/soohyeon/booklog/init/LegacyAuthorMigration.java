//package com.soohyeon.booklog.init;
//
//import com.soohyeon.booklog.domain.Author;
//import com.soohyeon.booklog.repository.AuthorRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class LegacyAuthorMigration implements CommandLineRunner {
//
//    private final JdbcTemplate jdbcTemplate;
//    private final AuthorRepository authorRepository;
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//        Long alreadyMigrated = jdbcTemplate.queryForObject(
//                "SELECT COUNT(*) FROM book_author", Long.class);
//
//        if (alreadyMigrated != null && alreadyMigrated > 0) {
//            log.info("[마이그레이션] book_author에 이미 데이터가 있어 건너뜁니다.");
//            return;
//        }
//
//        // JPA 엔티티는 더 이상 author 컬럼을 모르므로, JdbcTemplate으로 직접 레거시 값 조회
//        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
//                "SELECT id, author FROM book WHERE author IS NOT NULL AND author <> ''");
//
//        if (rows.isEmpty()) {
//            log.info("[마이그레이션] 마이그레이션할 레거시 author 데이터가 없습니다.");
//            return;
//        }
//
//        int migratedCount = 0;
//        for (Map<String, Object> row : rows) {
//            Long bookId = ((Number) row.get("id")).longValue();
//            String authorName = ((String) row.get("author")).trim();
//
//            if (authorName.isEmpty()) continue;
//
//            Author author = authorRepository.findByName(authorName)
//                    .orElseGet(() -> authorRepository.save(new Author(authorName)));
//
//            jdbcTemplate.update(
//                    "INSERT INTO book_author (book_id, author_id) VALUES (?, ?)",
//                    bookId, author.getId());
//
//            migratedCount++;
//        }
//
//        log.info("[마이그레이션] 총 {}건의 도서-저자 연결을 생성했습니다.", migratedCount);
//    }
//}