package com.soohyeon.booklog.init;

import com.soohyeon.booklog.domain.Book;
import com.soohyeon.booklog.domain.Member;
import com.soohyeon.booklog.domain.MemberRole;
import com.soohyeon.booklog.domain.ReadingLog;
import com.soohyeon.booklog.init.BookDummyData.BookSeed;
import com.soohyeon.booklog.repository.BookRepository;
import com.soohyeon.booklog.repository.MemberRepository;
import com.soohyeon.booklog.repository.ReadingLogRepository;
import com.soohyeon.booklog.service.BookRelationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TestDataInit implements ApplicationRunner {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final ReadingLogRepository readingLogRepository;
    private final BookRelationResolver relationResolver;

    // 시드에 태그가 비어있을 때 제목/한줄요약에서 키워드로 추론 (시드 전용 편의 기능)
    private static final Map<String, String> TAG_KEYWORDS = new LinkedHashMap<>();

    static {
        TAG_KEYWORDS.put("소설", "소설");
        TAG_KEYWORDS.put("에세이", "에세이");
        TAG_KEYWORDS.put("SF", "SF");
        TAG_KEYWORDS.put("미스터리", "미스터리");
        TAG_KEYWORDS.put("스릴러", "미스터리");
        TAG_KEYWORDS.put("추리", "미스터리");
        TAG_KEYWORDS.put("심리", "심리");
        TAG_KEYWORDS.put("경제", "경제");
        TAG_KEYWORDS.put("역사", "역사");
        TAG_KEYWORDS.put("세계사", "역사");
        TAG_KEYWORDS.put("한국사", "역사");
        TAG_KEYWORDS.put("삼국지", "역사");
        TAG_KEYWORDS.put("철학", "철학");
        TAG_KEYWORDS.put("부처", "철학");
        TAG_KEYWORDS.put("자기계발", "자기계발");
        TAG_KEYWORDS.put("고전", "고전");
        TAG_KEYWORDS.put("논픽션", "논픽션");
        TAG_KEYWORDS.put("Spring", "IT");
        TAG_KEYWORDS.put("코딩", "IT");
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Member member1 = getOrCreateMember("user1", "1234", "김독서", MemberRole.USER);
        Member member2 = getOrCreateMember("user2", "1234", "이서재", MemberRole.USER);
        getOrCreateMember("admin", "admin", "관리자", MemberRole.ADMIN);

        // 이미 도서가 있으면 시딩 건너뜀 (재시작 시 중복 삽입 방지)
        if (bookRepository.count() > 0) {
            return;
        }

        List<BookSeed> seeds = BookDummyData.books();
        int half = seeds.size() / 2;
        LocalDate today = LocalDate.now();

        for (int i = 0; i < seeds.size(); i++) {
            BookSeed seed = seeds.get(i);

            Book book = new Book(seed.title(), seed.status(), seed.rating(), seed.summary(), seed.memo());
            book.setMemberId(i < half ? member1.getId() : member2.getId());
            book.getAuthors().addAll(relationResolver.resolveAuthors(seed.author()));

            String tagNames = (seed.tags() == null || seed.tags().isBlank()) ? inferTags(seed) : seed.tags();
            book.getTags().addAll(relationResolver.resolveTags(tagNames));

            Book saved = bookRepository.save(book);

            // 가장 최근 기록 날짜: 오늘 ~ 59일 전 사이에서 책마다 다르게
            saveLogs(saved, seed.logs(), today.minusDays(i % 60));
        }
    }

    /**
     * 시드의 독서 기록을 저장. 목록의 첫 항목이 가장 오래된 기록이 되도록
     * 마지막 기록을 latestDate로 두고 앞쪽 기록일수록 4일씩 과거로 배치한다.
     */
    private void saveLogs(Book book, List<String> logs, LocalDate latestDate) {
        if (logs == null) {
            return;
        }
        for (int j = 0; j < logs.size(); j++) {
            LocalDate date = latestDate.minusDays((long) (logs.size() - 1 - j) * 4);
            readingLogRepository.save(new ReadingLog(book, logs.get(j), date));
        }
    }

    private String inferTags(BookSeed seed) {
        String text = seed.title() + " " + (seed.summary() == null ? "" : seed.summary());
        Set<String> tags = new LinkedHashSet<>();
        TAG_KEYWORDS.forEach((keyword, tag) -> {
            if (text.contains(keyword)) tags.add(tag);
        });
        return tags.stream().limit(3).collect(Collectors.joining(","));
    }

    private Member getOrCreateMember(String loginId, String password, String name, MemberRole role) {
        return memberRepository.findByLoginId(loginId)
                .orElseGet(() -> memberRepository.save(new Member(loginId, password, name, role)));
    }
}