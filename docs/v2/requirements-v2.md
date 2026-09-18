# 📚 북로그(BookLog) v2.0 스펙 명세서 & 개발 일지

## 📋 스펙 명세

### 1. 도메인 모델 확장

#### 1-1. Member (신규)
* **ID**: 고유 식별 번호 (`Long`)
* **로그인 아이디**: `loginId` (`String`, 필수, 중복 불가)
* **비밀번호**: `password` (`String`, 필수)
* **이름**: `name` (`String`, 필수)
* **권한**: `role` (`MemberRole`: USER / ADMIN) — 관리자 기능 확장을 대비한 필드로 v2.0에서는 값만 부여 

#### 1-2. Book (필드 추가)
* 기존 v1.4 필드(`id`, `title`, `author`, `status`, `rating`, `summary`, `memo`)에 아래 필드 추가
* **소유자 ID**: `memberId` (`Long`, 필수) — 해당 도서를 등록한 회원의 식별자

### 2. 핵심 기능 요구사항

* **기능 1**: 로그인 (`GET/POST /login`) — 세션(`HttpSession`) 기반 인증
* **기능 2**: 로그아웃 (`POST /logout`) — 세션 무효화
* **기능 3**: 회원별 서재 조회 (`GET /books`) — 로그인한 회원이 등록한 도서만 조회
* **기능 4**: 회원별 도서 등록 (`GET/POST /books/add`) — 등록 시 로그인한 회원의 `memberId` 자동 부여
* **기능 5**: 회원별 도서 상세 조회 (`GET /books/{bookId}`) — 본인 소유가 아닌 경우 조회 불가
* **기능 6**: 회원별 도서 수정 (`GET/POST /books/{bookId}/edit`) — 본인 소유가 아닌 경우 수정 불가
* **기능 7**: 회원별 도서 삭제 (`POST /books/{bookId}/delete`) — 본인 소유가 아닌 경우 삭제 불가

### 3. 인증(Authentication) 요구사항

* **인증 방식**: 세션 기반 인증 (JWT 미사용)
    * 서버-클라이언트가 분리되지 않은 단일 MVC 애플리케이션 구조를 고려하여, JWT의 무상태 확장성보다 세션의 구현 단순성(로그인/로그아웃 즉시 반영, 별도 토큰 갱신 로직 불필요)을 우선 채택
* **로그인 실패 처리**: 아이디/비밀번호 불일치 시 `/login?error`로 리다이렉트하고, 뷰에서 오류 메시지 노출
* **회원 식별 전달 방식**: `@LoginMember` 커스텀 애노테이션 + `HandlerMethodArgumentResolver`를 통해 컨트롤러 파라미터로 로그인 회원의 `memberId`를 직접 주입 (세션 접근 코드 중복 제거)

### 4. 인가(Authorization) 및 보안 요구사항

* **접근 제어**: `LoginCheckInterceptor`를 통해 `/books/**` 하위 경로에 대해 비로그인 사용자의 접근을 `/login`으로 리다이렉트
* **소유권 검증 (IDOR 방지)**:
    * 도서 상세 조회/수정/삭제 요청 시, URL의 `bookId`뿐 아니라 로그인 회원의 `memberId`를 함께 검증
    * `BookRepository.findByIdAndMemberId(id, memberId)`를 통해 "존재하지만 본인 소유가 아닌 도서"에 대한 접근을 차단
    * 검증 실패 시 `IllegalArgumentException`을 발생시키며, 실제 수정/삭제 로직은 호출되지 않음을 원칙으로 함
* **관리자 권한 준비**: `MemberRole` enum을 도입하여 이후 관리자 전용 조회(`BookRepository.findAll()` 활용)와 일반 회원 조회(`findAllByMemberId()`)를 분리 확장할 수 있는 구조를 v2.0 시점에 미리 확보

### 5. 데이터 격리 요구사항

* 동일한 도서 목록/검색/정렬 기능(v1.3~v1.4)은 모두 로그인한 회원의 `memberId` 범위 내에서만 동작해야 함
* `searchBooks(memberId, status, keyword, sort)`와 같이 기존 검색·필터·정렬 파라미터에 `memberId` 조건이 결합되어야 하며, 필터링/정렬 로직 자체(`filterByStatus`, `filterByKeyword`, `getComparator`)는 v1.3에서 구현한 내용을 그대로 재사용

### 6. 테스트 요구사항

* **Service 계층 테스트**: Mockito 기반 단위 테스트로 `BookRepository`를 mock 처리하여 서비스의 소유권 검증 로직(정상 케이스/예외 케이스)을 격리 검증
    * 본인 소유 도서 수정/삭제 성공 케이스
    * 타인 소유 도서 수정/삭제 시도 시 예외 발생 및 실제 리포지토리 변경 메서드가 호출되지 않음을 `verify(..., never())`로 검증
* **Repository 계층 테스트**: Mock 없이 실제 `MemoryBookRepository` 객체(Fake Object)를 사용하여 회원별 데이터 격리가 실제로 동작하는지 검증
    * 동일 저장소에 여러 회원의 데이터가 섞여 있을 때 `findAllByMemberId`/`findByIdAndMemberId`가 정확히 필터링되는지 확인
    * 테스트 간 격리를 위해 `@BeforeEach`에서 저장소를 초기화
* **테스트 데이터 구성**: 더미 데이터는 최소 2명 이상의 회원에게 분산 배정하여, 회원별 격리가 실제 화면에서도 육안으로 확인 가능하도록 구성

---

### 🔗 관련 문서 바로가기
* [🖼️ v2.0 결과 화면(Screenshots) 보러가기](screenshots-v2)
* [🗺️ v2.0 개발 일지 보러가기](devlog-v2.md)
* [🏠 메인 README로 돌아가기](../../README.md)