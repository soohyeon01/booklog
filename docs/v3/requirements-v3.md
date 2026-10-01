# 📚 북로그(BookLog) v3.0 스펙 명세서 & 개발 일지

## 📋 스펙 명세

### 1. 영속성 계층 전환 (인메모리 → JPA/MySQL)

* **ORM**: Spring Data JPA (Hibernate)
* **DBMS**: MySQL 8.0
* **스키마 관리**: `spring.jpa.hibernate.ddl-auto=update` (개발 단계 기준, 엔티티 변경 시 테이블 자동 반영)
* **연결 정보 보안**: 데이터베이스 계정/비밀번호는 `application.properties`에 평문으로 두지 않고 `${DB_USERNAME}`, `${DB_PASSWORD}` 환경변수로 분리하여 관리

### 2. 도메인 모델 확장

#### 2-1. Book (영속 엔티티 전환 및 필드 변경)
* 기존 v2.0 필드(`id`, `memberId`, `title`, `status`, `rating`, `summary`, `memo`)를 `@Entity`로 전환
* **등록일**: `createdAt` (`LocalDateTime`) — JPA Auditing(`@CreatedDate`)을 통해 최초 저장 시점에 자동 기록, 이후 수정 불가(`updatable = false`)
* **저자(author)**: 기존 단일 `String` 필드를 제거하고, `Author`와의 N:M 연관관계로 전환 (책 한 권에 여러 저자 등록 가능)
* **태그(tag)**: `Tag`와의 N:M 연관관계 추가 (책 한 권에 여러 태그 등록 가능)
* **독서 상태(status)**: `@Enumerated(EnumType.STRING)`으로 매핑하여 enum 순서 변경에 안전하도록 구성
* **메모(memo)**: 컬럼 길이를 `varchar(500)`으로 명시하여 검증 규칙(`@Size(max=500)`)과 실제 저장 길이의 불일치 방지

#### 2-2. Member (영속 엔티티 전환)
* 기존 v2.0 필드(`id`, `loginId`, `password`, `name`, `role`)를 `@Entity`로 전환
* `loginId`에 DB 레벨 `UNIQUE` 제약 추가 — 애플리케이션 검증뿐 아니라 동시 가입 시도 시 DB 레벨에서도 중복 아이디를 차단

#### 2-3. Author (신규)
* **ID**: 고유 식별 번호 (`Long`)
* **이름**: `name` (`String`, 필수, 중복 불가) — 동일 저자명이 여러 책에 등장해도 하나의 행으로 재사용

#### 2-4. Tag (신규)
* **ID**: 고유 식별 번호 (`Long`)
* **태그명**: `name` (`String`, 필수, 중복 불가) — 자유로운 태그명을 등록할 수 있도록 설계

#### 2-5. ReadingLog (신규)
* **ID**: 고유 식별 번호 (`Long`)
* **연결 대상**: `book` (`Book`과 N:1, 필수) — 회원이 아닌 **도서에 직접 연결**하여, 책의 소유권 변경과 무관하게 해당 책에 대한 기록만 누적되도록 설계
* **기록 내용**: `content` (`String`)
* **작성 시각**: `readingDate` (`LocalDateTime`) — JPA Auditing으로 자동 기록, 추후 "월별 독서 추이" 등 시계열 통계 집계에 사용 예정

### 3. 연관관계 설계 요구사항

* **Book ↔ Author**: N:M, `book_author` 조인테이블(`book_id`, `author_id` 복합 UNIQUE)을 통해 연결. `@JoinTable`로 자동 생성, 별도 엔티티 클래스 불필요
* **Book ↔ Tag**: N:M, `book_tag` 조인테이블(`book_id`, `tag_id` 복합 UNIQUE)을 통해 연결
* **Book ↔ ReadingLog**: 1:N, `ReadingLog`가 `book_id`(FK)를 보유. Member가 아닌 Book을 기준으로 연결하여 데이터 무결성 확보 (회원-책 소유권 불일치로 인한 모순 데이터 방지)
* **Member ↔ Book**: 기존 v2.0과 동일하게 `memberId`(Long) 단순 FK 값 유지 — 연관관계 승격(`@ManyToOne`)은 Query 최적화 단계에서 다룰 예정

### 4. Repository 리팩토링 요구사항

* 기존 `BookRepository`/`MemberRepository`의 커스텀 메서드(`update(Long, Book)`, `delete(Long)`)는 Spring Data JPA 명명 규칙과 호환되지 않아 제거
* `JpaRepository<Book, Long>`, `JpaRepository<Member, Long>`를 상속하는 구조로 전환하고, `findAllByMemberId`, `findByIdAndMemberId`, `findByLoginId` 등은 메서드 이름 기반 쿼리 파생(Query Derivation)으로 대체
* 수정 로직은 영속성 컨텍스트의 변경 감지(Dirty Checking)를 활용 — 엔티티 조회 후 setter로 필드를 변경하면 트랜잭션 커밋 시 자동으로 `UPDATE` 쿼리 발생
* 삭제 로직은 `JpaRepository.delete(entity)`를 사용 (ID가 아닌 조회된 엔티티 자체를 인자로 전달)
* `BookServiceImpl`의 쓰기 메서드(`saveBook`, `updateBook`, `deleteBook`)에 `@Transactional`을 명시적으로 부여하고, 조회 메서드는 클래스 레벨 `@Transactional(readOnly = true)`로 기본값 설정

### 5. 레거시 데이터 마이그레이션 요구사항

* `Book.author`(String) 필드 제거 이전에 저장되어 있던 레거시 데이터를 `Author` 테이블 및 `book_author` 조인테이블로 이관
* JPA 엔티티는 더 이상 `author` 컬럼을 매핑하지 않으므로, `JdbcTemplate`을 이용해 DB에 직접 SQL을 실행하여 레거시 컬럼 값을 조회
* 동일 저자명은 중복 생성하지 않고 기존 `Author` 행을 재사용(find-or-create)
* 애플리케이션 재시작 시 중복 마이그레이션이 발생하지 않도록 `book_author` 테이블의 데이터 존재 여부를 먼저 확인하는 로직 포함
* 마이그레이션 완료 후에는 1회성 코드이므로 비활성화(주석 처리) 처리

### 6. 더미데이터 구조 개선 요구사항

* 기존 `BookDummyData`가 `Book` 엔티티 생성자를 직접 호출하는 구조라, 엔티티 필드가 변경될 때마다 더미데이터 코드가 연쇄적으로 깨지는 문제가 있었음
* Book 엔티티와 결합되지 않는 순수 데이터 구조(BookSeed record)로 더미데이터를 분리하고, 실제 Book/Author 엔티티 조립은 TestDataInit에서 전담하도록 역할을 분리할 예정 — 미완료. 현재는 컴파일 오류를 피하기 위해 BookDummyData/TestDataInit의 더미데이터 삽입 로직을 전체 주석 처리한 임시 상태

### 7. 테스트 요구사항 (진행 예정)

* 인메모리 구현체(`MemoryBookRepository`) 제거에 따라 기존 `MemoryBookRepositoryTest`는 더 이상 유효하지 않으며, `@DataJpaTest` 기반으로 재작성 필요 — **미완료**
* `BookServiceImplTest`(Mockito 기반)는 Repository 인터페이스 시그니처가 유지되는 범위 내에서는 재사용 가능하나, `authorNames` 파라미터 추가분에 대한 테스트 케이스 보강 필요 — **미완료**

### 8. 남은 작업 (v3.0 완료 조건)

* **Query 최적화**: `Book`의 저자(`authors`)/태그(`tags`) 지연 로딩 시 발생하는 N+1 문제를 의도적으로 재현하고, `fetch join` 또는 `@EntityGraph`로 해결
* **Member ↔ Book 연관관계 승격**: 단순 `memberId`(Long) FK 값을 `@ManyToOne Member member`로 전환
* 위 두 항목이 완료되어야 v3.0 마일스톤을 완료로 표시

---

### 🔗 관련 문서 바로가기
* [🗺️ v3.0 개발 일지 보러가기](dev-log-v3.md)
* [📝 v2.0 스펙 명세서 보러가기](../v2/requirements-v2.md)
* [🏠 메인 README로 돌아가기](../../README.md)