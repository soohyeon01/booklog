# 🗺️ 개발 일지 (Daily Log)

### 🟡 예정된 기능 (To-Do) -> 🟢 모두 완료 (Done)

### 2026.10.14 (Wed)
- [v3.1-final] 독서 기록 렌더링 오류 수정 및 최종 동작 확인
    - [x] 도서 상세 화면의 독서 기록 섹션에서 발생하던 Thymeleaf 렌더링 오류 수정
    - [x] 독서 기록 추가/삭제 및 상세 화면 타임라인 정상 동작 확인

### 2026.10.13 (Tue)
- [v3.1-seed] 더미데이터 구조 개선 및 복구
    - [x] `BookDummyData`를 `Book` 엔티티와 결합되지 않는 `BookSeed` record로 분리 (6개 인자 보조 생성자로 기존 80권 항목 호환)
    - [x] `TestDataInit`을 `@PostConstruct`에서 `ApplicationRunner`로 전환하여 `@Transactional`이 실제로 적용되도록 수정
    - [x] 시딩 중복 방지 가드를 `bookRepository.count() > 0` 기준으로 구성
    - [x] 제목/한줄요약 키워드 기반 태그 자동 추론, 독서 기록 시드를 최근 2~3개월에 분산 배치

### 2026.10.08 (Thu)
- [v3.1-readinglog] 독서 기록 기능 구현
    - [x] 도서 상세 하단에 독서 기록 타임라인 섹션 추가 (기록 수 뱃지, 추가 폼, 최신순 목록)
    - [x] `ReadingLogService`, `ReadingLogController`(`POST /books/{bookId}/logs`, `POST /books/{bookId}/logs/{logId}/delete`) 구현, PRG 패턴 유지
    - [x] 기록 날짜를 작성 시각 자동 기록에서 **사용자 지정 읽은 날짜(`readingDate`, `LocalDate`)** 로 확정 — 기본값 오늘, 미래 날짜는 서버/브라우저 양쪽에서 차단
    - [x] 기록 추가/삭제 시 책 소유권 검증, 삭제는 `logId`가 해당 `bookId` 소속인지 함께 검증하여 타 회원 기록 ID 조합 우회 차단
    - [x] 도서 삭제 시 해당 도서의 독서 기록을 먼저 삭제하여 FK 제약 위반 방지

### 2026.10.07 (Wed)
- [v3.1-tag-fix] 태그 수정 흐름 검증 및 스키마 정합성 보정
    - [x] 도서 수정 시 태그 재매핑 흐름(`updateBook` → `resolveTags`) 동작 검증
    - [x] `Tag` 필드명 변경(`genre` → `name`) 이후 DB에 남아있던 레거시 `genre` 컬럼 제거

### 2026.10.06 (Tue)
- [v3.1-tag] 태그 도메인 기능화
    - [x] 저자/태그 문자열 → 엔티티 변환 로직을 `BookRelationResolver`로 분리하여 find-or-create 로직 공통화
    - [x] `BookForm`에 콤마 구분 태그 입력 필드 추가, 등록/수정 폼에 태그 입력란 반영 (수정 폼은 기존 태그를 콤마 문자열로 미리 채움)
    - [x] 목록·상세 화면에 `#태그` 뱃지 표시, 뱃지 클릭 시 키워드 검색으로 연결
    - [x] 키워드 검색 대상을 제목·저자에서 태그명까지 확장 (`searchBooks` 시그니처 유지)
    - [x] `TagRepository.findByGenre`를 `findByName`으로 교체 (필드명 변경 반영)

---

## 🛠️ 기술적 예외 처리 및 트래픽 기록 (Troubleshooting)

### 📌 2026.10.07 - 태그 저장 시 `Field 'genre' doesn't have a default value`
- **증상**: 도서 수정 시 새 태그를 저장하는 과정에서 `DataIntegrityViolationException` 발생. 로그의 SQL은 `insert into tag (name) values (?)`로 `name`만 삽입하는데 MySQL이 `genre` 컬럼 값이 없다며 거부함.
- **원인**:
    - `Tag` 엔티티를 처음 만들 때 `genre`(NOT NULL)로 매핑해 테이블이 생성됨.
    - 이후 필드를 `name`으로 바꿨지만 `ddl-auto=update`는 컬럼을 **추가만 하고 삭제/이름 변경은 하지 않아**, `name`은 새로 생기고 `genre`(NOT NULL, 기본값 없음)가 그대로 남음.
- **해결**: `DESCRIBE tag`로 두 컬럼이 공존함을 확인한 뒤, `ALTER TABLE tag DROP COLUMN genre;`로 레거시 컬럼 제거.
- **성과 및 배운 점**
    - 코드(엔티티)가 아니라 DB 스키마가 원인인 문제는 에러 메시지의 SQL과 실제 테이블 구조를 비교해야 보임.
    - `ddl-auto=update`는 필드 변경을 DB에 완전히 반영하지 못하므로, 스키마 변경이 잦아지면 Flyway 같은 마이그레이션 도구가 필요하다는 점을 체감함.

### 📌 2026.10.08 - 독서 기록 날짜 필드의 생성자/Auditing 불일치
- **증상**: `ReadingLog` 생성자는 날짜 파라미터(`LocalDate`)를 받지만 필드에 대입하지 않았고, 필드는 `LocalDateTime` + `@CreatedDate`여서 서비스에서 넘긴 날짜가 어디에도 저장되지 않음.
- **원인**: "기록 작성 시각을 자동으로 남길 것인가, 사용자가 읽은 날짜를 지정할 것인가"라는 의미를 정하지 않은 채 두 방식이 섞여 있었음. `@CreatedDate`는 저장 시점에 값을 자동 주입하므로 생성자로 넣은 값이 유지되지 않음.
- **해결**: 월별 독서량 집계가 목적이므로 날짜의 의미를 **사용자가 지정하는 읽은 날짜**로 확정하고, 필드를 `LocalDate`로 변경, `@CreatedDate`/`@EntityListeners` 제거, 생성자에서 실제로 대입. 정렬 메서드와 템플릿의 필드명도 함께 수정.
- **성과 및 배운 점**
    - 필드의 타입과 어노테이션을 고르기 전에 그 값이 "시스템이 기록하는 값인가, 사용자가 입력하는 값인가"를 먼저 정해야 함을 학습함.

### 📌 2026.10.08 - 도서 삭제 시 독서 기록 FK 제약과 Repository 메서드 누락
- **증상**: 서비스에서 `readingLogRepository.deleteAllByBookId(...)`를 호출하는 코드에서 컴파일 에러 발생. 또한 메서드를 추가하기 전에는 기록이 달린 도서를 삭제하면 FK 제약 위반이 발생할 구조였음.
- **원인**:
    - `ReadingLogRepository`에 해당 메서드를 선언하지 않았고, `BookServiceImpl`에 `ReadingLogRepository` 필드도 없었음.
    - `ReadingLog`가 `Book`을 단방향으로 참조만 해서, `book_author`/`book_tag`와 달리 도서를 삭제해도 기록이 자동 삭제되지 않음.
- **해결**: Repository에 `deleteAllByBookId(Long bookId)`를 선언(Spring Data JPA가 이름으로 구현 생성)하고, 서비스 필드를 추가한 뒤 소유권 검증을 통과한 다음 기록을 먼저 삭제하고 도서를 삭제하도록 수정.
- **성과 및 배운 점**
    - 다대다 조인테이블은 관계의 주인(`Book`)이 정리해주지만, 단방향 `@ManyToOne`으로 참조하는 쪽의 데이터는 직접 정리해야 함을 확인함.

### 📌 2026.10.14 - 상세 화면 `Property or field 'readingDate' cannot be found on null`
- **증상**: 도서 상세 페이지 진입 시 Thymeleaf 템플릿(`/books/book`, 141행)의 `#temporals.format(log.readingDate, 'yyyy-MM-dd')`에서 500 오류 발생. 책/저자/태그/기록 조회 쿼리는 모두 정상 실행된 뒤 렌더링 단계에서 실패함.
- **원인**: log가 존재하지 않는 위치에서 참조됨
- **해결**: 등록 폼 내부에서 log.readingDate를 참조하는 잘못된 div를 제거함.
