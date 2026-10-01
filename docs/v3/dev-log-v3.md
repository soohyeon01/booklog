# 🗺️ 개발 일지 (Daily Log)

### 🟡 예정된 기능 (To-Do) -> 🟢 모두 완료 (Done)

### 2026.09.30 (Tue)
- [v3.0-migration] 레거시 데이터 마이그레이션 및 더미데이터 구조 개선
    - [x] 레거시 `author`(String) 컬럼 값을 `Author` 테이블 및 `book_author` 조인테이블로 이관하는 1회성 마이그레이션(`LegacyAuthorMigration`, `JdbcTemplate` 기반) 구현
    - [x] 동일 저자명 재사용(find-or-create) 및 중복 실행 방지 가드 적용
    - [x] 마이그레이션 완료 후 해당 컴포넌트 비활성화(주석 처리)
    - [ ] `BookDummyData`를 `Book` 엔티티와 결합되지 않는 `BookSeed` record로 분리하는 리팩터링 — 아직 착수하지 않음. 엔티티 변경으로 컴파일이 깨진 `BookDummyData`/`TestDataInit`의 더미데이터 삽입 로직은 우선 전체 주석 처리하여 애플리케이션 구동만 가능하도록 임시 조치

### 2026.09.29 (Mon)
- [v3.0-domain] 저자(Author) 반영 로직 전면 리팩토링
    - [x] `BookService`/`BookServiceImpl`의 `saveBook`, `updateBook`에 콤마로 구분된 저자명 문자열(`authorNames`) 파라미터 추가
    - [x] 입력된 저자명을 `Author` 엔티티로 변환하는 `resolveAuthors` 메서드 구현 (기존 저자명 재사용, 신규 저자 생성)
    - [x] `BookController`의 `add`/`edit` 흐름에 `authorNames` 전달 로직 반영, 수정 폼 진입 시 `Set<Author>` → 콤마 구분 문자열 변환 로직 추가
    - [x] `books.html`/`book.html`의 저자 표시 영역을 `Set<Author>` 순회 방식으로 수정

### 2026.09.28 (Sun)
- [v3.0-model] Author / Tag / ReadingLog 엔티티 설계 및 연관관계 모델링
    - [x] ERD 설계: 회원(1)-도서(N), 도서-저자(N:M), 도서-태그(N:M), 도서(1)-독서기록(N) 구조로 확정
    - [x] `Book.author`(String) 필드 제거, `Author` 엔티티 신설 및 `@ManyToMany` + `@JoinTable`(`book_author`)로 전환
    - [x] `Tag` 엔티티 및 `@ManyToMany` + `@JoinTable`(`book_tag`) 추가
    - [x] `ReadingLog` 엔티티 추가 — Member가 아닌 Book에 직접 연결하여 소유권 변경과 무관하게 책 단위로 기록이 누적되도록 설계
    - [x] `AuthorRepository`, `TagRepository`, `ReadingLogRepository` 추가

### 2026.09.26 (Sat)
- [v3.0-ux] JPA Auditing 기반 등록일 기능 추가 (v1.4 "등록일 및 UX 개선" 항목 완료 처리)
    - [x] `@EnableJpaAuditing` 활성화
    - [x] `Book`에 `@CreatedDate` + `@EntityListeners(AuditingEntityListener.class)`로 `createdAt` 필드 추가, 저장 시점에 자동 기록되도록 구성
    - [x] `books.html`(목록), `book.html`(상세)에 등록일 표시 — 엔티티 전환 이전 더미데이터는 `createdAt`이 `null`일 수 있음을 고려해 null 분기 처리

### 2026.09.24 (Thu)
- [v3.0-persistence] Repository JPA 전환 및 실제 MySQL CRUD 검증
    - [x] `MemoryBookRepository`, `MemoryMemberRepository` 삭제
    - [x] `BookRepository`/`MemberRepository`를 `JpaRepository` 상속 구조로 전환, 커스텀 `update`/`delete(Long)` 메서드 제거
    - [x] `BookServiceImpl`의 `updateBook`/`removeBook`을 변경 감지(Dirty Checking) 및 `delete(entity)` 방식으로 재작성, `@Transactional` 명시
    - [x] `TestDataInit`은 Repository 인터페이스를 그대로 재사용하여 코드 수정 없이 JPA 기반으로 전환
    - [x] 실제 MySQL 환경에서 등록/조회/수정/삭제 및 회원별 서재 격리 전체 시나리오 검증 완료

### 2026.09.21 (Mon)
- [v3.0-entity] Book / Member 영속 엔티티 전환
    - [x] `Book`, `Member`를 `@Entity`로 전환, `@Id`/`@GeneratedValue(IDENTITY)` 매핑
    - [x] `BookStatus`, `MemberRole`을 `@Enumerated(EnumType.STRING)`으로 매핑하여 enum 순서 변경에 안전하도록 구성
    - [x] `memo` 컬럼 길이를 `varchar(500)`으로 명시 (기본값 `varchar(255)`로 인한 검증-저장 불일치 방지)
    - [x] `Member.loginId`에 DB 레벨 `UNIQUE` 제약 추가

### 2026.09.20 (Sun)
- [v3.0-setup] MySQL 연결 및 환경 구성
    - [x] 로컬 MySQL 데이터베이스(`booklog`) 생성
    - [x] `spring.jpa.hibernate.ddl-auto=none` 상태로 엔티티 없이 커넥션 자체만 우선 검증 (엔티티 매핑 문제와 커넥션 문제를 분리해 디버깅하기 위함)
    - [x] DB 계정/비밀번호를 `application.properties`에 평문으로 두지 않고 환경변수(`${DB_USERNAME}`, `${DB_PASSWORD}`)로 분리

---

## 🛠️ 기술적 예외 처리 및 트래픽 기록 (Troubleshooting)

### 📌 2026.09.20 - `application.properties`에 DB 비밀번호 평문 노출 위험
- **증상**: 로컬 개발용 `application.properties`에 실제 MySQL 비밀번호가 평문으로 작성되어 있었음.
- **원인**: 연결 설정을 빠르게 검증하기 위해 임시로 실제 값을 직접 입력한 뒤, 이를 환경변수로 분리하지 않고 그대로 유지.
- **해결**: `spring.datasource.password=${DB_PASSWORD}` 형태로 환경변수 참조로 변경하고, 로컬 실행 시 IDE Run Configuration의 환경변수로 실제 값을 주입하도록 수정.
- **성과 및 배운 점**
    - 공개 저장소에 커밋되는 설정 파일에는 민감정보를 직접 작성하지 않아야 하며, 이는 "나중에 지우면 된다"가 아니라 애초에 커밋 이전 단계에서 걸러야 하는 문제임을 체감함.

### 📌 2026.09.24 - 커스텀 Repository 메서드와 Spring Data JPA 명명 규칙 불일치
- **증상**: 기존 `BookRepository` 인터페이스의 `update(Long bookId, Book updateParam)`, `delete(Long bookId)` 메서드를 그대로 둔 채 `JpaRepository`를 상속하자 애플리케이션 구동 시 구현체를 생성하지 못함.
- **원인**: `delete(Long)`은 Spring Data JPA가 인식하는 명명 규칙(`deleteById(ID id)`)과 다른 이름이었고, `update(Long, Book)`과 같은 시그니처는 애초에 JPA의 수정 방식(변경 감지)과 맞지 않는 설계였음.
- **해결**: 두 메서드를 제거하고, 수정은 엔티티 조회 후 필드를 직접 변경해 트랜잭션 커밋 시 자동 반영되도록, 삭제는 `JpaRepository.delete(entity)`를 사용하도록 서비스 계층 로직을 재작성.
- **성과 및 배운 점**
    - 인메모리 구현체를 전제로 설계된 Repository 인터페이스를 JPA로 그대로 이식할 수 없으며, 영속성 프레임워크가 제공하는 관용적인 방식(Dirty Checking)에 맞춰 설계를 다시 검토해야 함을 학습함.

### 📌 2026.09.24 - 삭제(POST) 엔드포인트를 주소창으로 직접 테스트할 때 발생한 405 오류
- **증상**: 타인 소유 도서에 대한 접근 제어(IDOR 방지)를 검증하기 위해 삭제 URL을 브라우저 주소창에 직접 입력하자 `HttpRequestMethodNotSupportedException`(GET method not supported) 발생. 본인 소유 도서에서도 동일하게 재현됨.
- **원인**: 삭제 엔드포인트는 `@PostMapping`으로 정의되어 있는데, 주소창 직접 입력은 항상 GET 요청으로 전송되어 라우팅 단계에서부터 걸러진 것이며, 소유권 검증 로직과는 무관한 현상이었음.
- **해결**: GET으로 매핑된 엔드포인트(상세조회, 수정 폼 진입)는 주소창 테스트로 충분하다고 판단하고, POST 엔드포인트(수정 제출, 삭제)는 `curl`/Postman으로 로그인 세션 쿠키를 포함해 직접 요청을 보내는 방식으로 검증 절차를 분리.
- **성과 및 배운 점**
    - 접근 제어를 검증할 때는 애플리케이션의 인가 로직뿐 아니라, 테스트 도구가 실제로 어떤 HTTP 메서드를 전송하는지까지 함께 고려해야 정확한 원인 분석이 가능함을 확인함.

### 📌 2026.09.29 - `Book.author`(String) 제거로 인한 연쇄적 컴파일/런타임 에러
- **증상**: `Book.author` 필드를 제거하고 `Set<Author> authors`로 전환한 뒤, `BookServiceImpl`의 검색 필터링 로직, 수정 로직, 컨트롤러의 수정 폼 진입 로직, Thymeleaf 템플릿 등 여러 지점에서 `getAuthor()` 호출로 인한 에러가 순차적으로 발생.
- **원인**: 하나의 필드 타입 변경이 해당 필드를 참조하는 모든 계층(Service 필터링 로직, 컨트롤러의 폼 바인딩, 뷰 템플릿)에 연쇄적으로 영향을 미쳤으나, 한 번에 모든 참조 지점을 파악하지 못함.
- **해결**: 콤마로 구분된 저자명 문자열을 입력받아 `Author` 엔티티로 변환하는 `resolveAuthors`(find-or-create) 메서드를 Service 계층에 도입하여 입력 경로를 일원화하고, 출력 경로(폼 미리채우기, 화면 표시)는 `Set<Author>`를 문자열/목록으로 변환하는 별도 로직으로 분리.
- **성과 및 배운 점**
    - 도메인 필드의 타입을 변경할 때는 해당 필드가 읽기(출력)와 쓰기(입력) 양쪽에서 어떻게 쓰이는지 먼저 추적한 뒤 변경 범위를 가늠해야 하며, 단순 문자열에서 연관관계로의 전환은 생각보다 넓은 범위에 영향을 미친다는 점을 체감함.

### 📌 2026.09.30 - JPA 매핑이 끊긴 레거시 컬럼에 대한 마이그레이션 접근 방식
- **증상**: `Book` 엔티티에서 `author` 컬럼 매핑을 제거한 뒤에도 MySQL의 `book` 테이블에는 레거시 `author` 값이 그대로 남아있었으나, JPA 엔티티로는 더 이상 이 컬럼에 접근할 수 없는 상태였음.
- **원인**: `ddl-auto=update`는 기존 컬럼을 삭제하지 않으므로 레거시 데이터가 테이블에 남아있지만, 엔티티 매핑이 끊긴 컬럼은 JPQL/Spring Data 쿼리 메서드로 조회할 방법이 없음.
- **해결**: `JdbcTemplate`으로 DB에 직접 SQL을 실행해 레거시 `author` 컬럼 값을 조회하고, 이를 `Author` 테이블 및 `book_author` 조인테이블로 옮기는 1회성 마이그레이션 컴포넌트(`CommandLineRunner`)를 작성. 재실행 시 중복 삽입을 방지하기 위해 `book_author` 데이터 존재 여부를 먼저 확인하는 가드를 추가.
- **성과 및 배운 점**
    - ORM 매핑에서 제외된 컬럼에 접근해야 하는 경우, JPA가 아닌 더 로우레벨의 수단(JdbcTemplate)을 보조적으로 활용할 수 있다는 것과, 1회성 마이그레이션 코드에도 멱등성(idempotency)을 고려한 가드가 필요하다는 점을 학습함.