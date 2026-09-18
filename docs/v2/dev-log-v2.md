# 🗺️ 개발 일지 (Daily Log)

### 🟡 예정된 기능 (To-Do) -> 🟢 모두 완료 (Done)

### 2026.09.18 (Fri)
- [v2.0-final] 회원별 서재 기능 및 테스트 코드 보강
    - [x] `MemoryBookRepositoryTest`의 상태 초기화 방식을 `@AfterEach` → `@BeforeEach`로 변경하여 테스트 격리(test isolation) 강화
    - [x] Service 계층(Mockito mock)과 Repository 계층(Fake Object) 테스트의 책임을 명확히 분리: "서비스가 올바른 memberId로 리포지토리를 호출하는가"는 Mockito로, "리포지토리가 실제로 회원별 데이터를 격리하는가"는 Fake Object로 검증

### 2026.09.17 (Thu)
- [v2.0-security] 도서 소유권 검증 및 IDOR 방지
    - [x] `Book` 도메인에 `memberId` 필드 추가 — 기존 생성자는 그대로 유지하여 더미데이터 호환성 확보
    - [x] `BookRepository`에 `findAllByMemberId(Long memberId)`, `findByIdAndMemberId(Long id, Long memberId)` 추가
    - [x] `BookServiceImpl`의 `updateBook`/`removeBook`에 소유권 검증 로직 추가: 본인 소유가 아니면 `IllegalArgumentException` 발생 후 실제 수정/삭제 로직은 호출되지 않도록 차단
    - [x] `BookController`의 모든 CRUD 메서드에 `@LoginMember Long memberId` 파라미터를 추가하고 Service 호출부에 전달
    - [x] `TestDataInit`에 테스트 회원 2명(`user1`, `user2`)을 분리 등록하고 더미 도서를 절반씩 배정 → 브라우저에서 계정별 서재 격리를 눈으로 직접 확인 가능하도록 구성

### 2026.09.13 (Sun)
- [v2.0-auth] 세션 기반 로그인/로그아웃 구현
    - [x] `Member` 도메인 및 `MemberRepository`/`MemoryMemberRepository` 구현 — 기존 `Book`/`BookRepository` 설계 패턴을 그대로 재사용
    - [x] `LoginController` 구현: `/login`(GET/POST), `/logout`(POST) 엔드포인트 및 `HttpSession` 기반 로그인 처리
    - [x] `@LoginMember` 커스텀 애노테이션 + `LoginMemberArgumentResolver`(`HandlerMethodArgumentResolver`) 구현 — 컨트롤러마다 `session.getAttribute(...)`를 반복하지 않도록 회원 id 주입 방식 캡슐화
    - [x] `LoginCheckInterceptor` 구현 및 `WebConfig`에 등록 — `/books/**` 경로 비로그인 접근 시 `/login`으로 리다이렉트
    - [x] `login/loginForm.html` 뷰 추가 (기존 `fragment/base` 레이아웃 재사용)

### 2026.9.10 (Thu)
- [v2.0-design] 회원 도메인 설계 및 아키텍처 결정
    - [x] 인메모리 구조(`MemoryBookRepository`)를 유지한 채 `Member` 개념을 얹는 방향으로 설계 — DB 도입과 회원 도입을 한 번에 하지 않고 관심사를 분리
    - [x] 향후 관리자 기능 확장을 고려해 `Member`에 `MemberRole`(USER/ADMIN) 필드를 미리 추가
    - [x] `BookRepository`에 전체 조회(`findAll`)와 회원별 조회(`findAllByMemberId`)를 함께 유지 — 추후 `AdminBookService`가 `findAll()`을, 일반 사용자 서비스가 `findAllByMemberId()`를 사용하도록 확장 여지 확보

---

## 🛠️ 기술적 예외 처리 및 트래픽 기록 (Troubleshooting)

### 📌 2026.9.10 - Member 도입 후 BookController 컴파일 에러
- **증상**: `BookService`/`BookRepository`의 메서드 시그니처에 `memberId`를 추가한 뒤, `BookController`의 `editForm`, `edit`, `delete` 메서드에서 컴파일 에러 발생.
- **원인**: `books()`, `add()`, `book()` 메서드에는 `@LoginMember Long memberId` 파라미터를 추가했지만, 나머지 세 메서드(`editForm`, `edit`, `delete`)는 기존 시그니처(`optionalToBook(bookId)`, `updateBook(bookId, updateParam)`, `removeBook(bookId)`)를 그대로 사용하고 있어 변경된 인터페이스와 불일치.
- **해결**: 세 메서드 모두에 `@LoginMember Long memberId` 파라미터를 추가하고, `optionalToBook(bookId, memberId)` / `updateBook(bookId, memberId, updateParam)` / `removeBook(bookId, memberId)` 형태로 호출부를 일괄 수정.
- **성과 및 배운 점**
    - 인터페이스 시그니처를 변경할 때는 구현체의 모든 호출부를 빠짐없이 점검해야 하며, 특히 유사한 CRUD 메서드가 여러 개 있을 때 일부만 수정하고 넘어가기 쉽다는 점을 체감함.

### 📌 2026.09.13 - `/login` 접근 시 뷰를 찾지 못하는 오류
- **증상**: `LoginController`의 `/login` GET 요청 처리 후 `"login/loginForm"` 뷰를 찾지 못해 오류 발생.
- **원인**: 컨트롤러 코드만 작성하고 대응하는 `templates/login/loginForm.html` 파일을 실제로 생성하지 않음.
- **해결**: 기존 `addForm.html` 등과 동일한 `fragment/base :: commonHead`/`mainHeader`/`mainFooter` 구조를 재사용하여 `login/loginForm.html`을 작성하고, `param.error` 쿼리 파라미터로 로그인 실패 메시지를 표시하도록 구성.
- **성과 및 배운 점**
    - 컨트롤러의 반환 뷰 이름과 실제 템플릿 파일 경로가 일치하는지 항상 함께 확인해야 함을 재확인.

### 📌 2026.09.17 - 더미데이터가 단일 회원 소유로 배정되어 격리 여부를 확인할 수 없는 문제
- **증상**: `TestDataInit`에서 모든 더미 도서를 테스트 회원 한 명에게만 배정하여, "로그인한 회원 본인의 책만 보이는지"를 브라우저에서 직접 확인할 방법이 없었음.
- **원인**: 초기 `TestDataInit` 구현 시 회원을 한 명만 생성하고 모든 더미 데이터를 해당 회원에게 일괄 배정.
- **해결**: 테스트 회원을 `user1`, `user2` 두 명으로 분리하고, 더미 도서 목록을 절반씩 나누어 배정하도록 수정. 이를 통해 서로 다른 계정으로 로그인했을 때 상대방의 서재가 보이지 않는지 직접 검증 가능해짐.
- **성과 및 배운 점**
    - 수동 QA(브라우저 확인)와 자동화 테스트(`BookServiceImplTest`)는 서로 다른 목적을 가지며, 회원별 격리처럼 "눈으로 봐야 체감되는" 기능은 더미데이터 설계 단계에서부터 최소 2개 이상의 계정을 고려해야 함을 학습함.

### 📌 2026.09.18 - 리포지토리 단위 테스트의 상태 누수 가능성
- **증상**: `MemoryBookRepositoryTest`에서 `@AfterEach`로 `clearStore()`를 호출하는 방식이, 테스트 도중 예외가 발생하면 정리 로직이 실행되지 않아 다음 테스트에 이전 데이터가 남을 수 있는 구조였음.
- **원인**: 저장소가 `static Map`을 사용하는 구조라 테스트 간 상태가 공유되며, 뒷정리(clean-up)를 "이전 테스트의 종료 시점"에 의존하고 있었음.
- **해결**: `@AfterEach` 대신 `@BeforeEach`에서 `clearStore()`를 호출하도록 변경하여, 이전 테스트의 종료 상태와 무관하게 매 테스트가 항상 깨끗한 상태에서 시작되도록 개선.
- **성과 및 배운 점**
    - 테스트 격리(test isolation)는 "정리를 누가, 언제 책임지는가"의 문제이며, 뒷정리를 이전 테스트가 아닌 다음 테스트 시작 시점에 두는 것이 더 견고한 설계임을 학습함.