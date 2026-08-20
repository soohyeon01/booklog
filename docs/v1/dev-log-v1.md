# 🗺️ 개발 일지 (Daily Log)

### 🟡 예정된 기능 (To-Do) -> 🟢 모두 완료 (Done)

### 2026.08.20 (Thu)
- [v1.4-validation] Spring Validation 적용 및 입력 DTO 분리
  - [x] `BookForm` DTO 분리: 도메인 객체 `Book`과 사용자 입력 데이터의 역할을 분리하고, 등록/수정 과정에서 검증 전용 DTO 사용
  - [x] Bean Validation 적용: `@NotBlank`, `@NotNull`, `@Size`, `@Min`, `@Max`를 활용하여 도서 입력값에 대한 서버 측 검증 구현
  - [x] `@Validated` + `BindingResult`를 활용한 Controller 검증 흐름 구축
  - [x] 검증 실패 시 입력 화면으로 재전송하여 사용자가 입력한 데이터와 오류 메시지를 유지
  - [x] 등록/수정 폼 Validation 구조 통일: `BookForm`을 공통 입력 DTO로 활용하여 동일한 검증 규칙 적용

### 2026.08.08 (Sat)
- [v1.3-Sort] 다중 정렬 기능 구현 및 UI/UX 고도화
  - [x] 다양한 정렬 기준 지원: 최신순(`id_desc`), 오래된순(`id_asc`), 제목 내림/올림차순(`title_desc`/`title_asc`), 평점 높은/낮은순(`rating_desc`/`rating_asc`) 6종 정렬 조건 구현
  - [x] Service 단 메서드 캡슐화: `searchBooks` 내 비대해진 로직을 `filterByStatus`, `filterByKeyword`, `getComparator` 헬퍼 메서드로 분리하여 단일 책임 원칙(SRP) 준수
  - [x] 단일 Stream 파이프라인 통합: 여러 번 생성되던 중간 List 리소스 낭비를 방지하고자 단일 Stream 체이닝으로 연산 최적화
  - [x] UI/UX 개선: 정렬 드롭다운(`<select>`)에 `onchange="changeSort(this.value)"` 연동으로 별도의 검색 버튼 클릭 없이 선택 시 즉시 정렬 반영
  - [x] 3개 조건 상태 보존: 검색어(`keyword`), 상태(`status`), 정렬(`sort`) 파라미터가 링크 및 폼 간 상호 유실되지 않도록 전면 바인딩 완료

### 2026.08.06 (Thu)
- [v1.3-search] 동적 검색 기능 추가 및 Query Parameter 상태 유지
  - [x] 복합 검색 백엔드 로직: Service 단에 `Stream API` 기반 키워드(제목/저자) 및 독서 상태(`status`) 교집합 검색 메서드(`searchBooks`) 구현
  - [x] 대소문자 무시(Case-Insensitive) 파이프라인 구축: `toLowerCase()`를 활용해 검색 편의성 증대
  - [x] Controller-View 파라미터 바인딩 보완: `@RequestParam String keyword`를 Model에 담아 뷰로 전달함으로써 검색 후 입력창 데이터 유지
  - [x] 검색어-필터 상태 유지(Parameter State Persistence):
    - 검색 폼 내 `<input type="hidden" name="status" th:value="${status}">` 추가로 검색 시 기존 필터 상태 보존
    - 상태 필터 버튼 쿼리 스트링에 `keyword=${keyword}` 동적 바인딩 적용으로 필터 전환 시 기존 검색어 보존

### 2026.07.09 (Thu)
- [v1.0-final] 순수 CRUD 및 PRG 패턴 확립
  - [x] `@ModelAttribute`를 활용한 데이터 자동 바인딩 및 저장 로직 구현
  - [x] 새로고침 중복 등록 버그 방지를 위한 PRG(Post-Redirect-Get) 패턴 도입
  - [x] `@PathVariable` 및 `Optional.orElseThrow()` 기반 안전한 상세 조회 로직 구현
  - [x] 상세 페이지 내 도서 수정 및 삭제 기능 연동 완료

- [v1.1] UX 고도화 및 데이터 흐름 최적화
  - [x] `addFlashAttribute` 도입: 세션 기반 일회성 알림 메시지 배달로 주소창 최적화 (`?status=true` 제거)
  - [x] `books.html`에서 제목 클릭 시 상세 페이지로 바로 이동하는 하이퍼링크(`<a>`) 연동
  - [x] 메인 목록 화면 내 행별 [삭제] 버튼 및 자바스크립트 `confirm` 얼럿창 추가

- [v1.2-final] 아키텍처 리팩터링 및 UI 전면 고도화
  - [x] URL 인코딩 방어: `RedirectAttributes.addAttribute()`의 템플릿 치환 방식을 채택하여 보안성 강화
  - [x] 타임리프 프래그먼트(`th:fragment`) 도입: header/footer 등 중복 UI 구조를 `base.html`로 모듈화하여 코드 가독성 및 유지보수성 향상
  - [x] 중복 UX 해결: 공통 헤더에서 목록 이동 링크를 제거하고, 화면별 목적에 맞는 네비게이션 버튼을 본문에 배치
  - [x] 스트림 필터링: 메인 화면 상단에 상태별 도서 권수 카운팅 대시보드 배치 및 `?status=READING` 쿼리 파라미터 동적 필터링 구현
  - [x] 예외 커스텀 페이지 구축: 잘못된 접근 시 톰캣 화이트라벨 에러 페이지 대신 `error/404.html` 및 `error/500.html` 커스텀 화면 노출

### 2026.07.07 (Tue)
- [x] 초기 세팅: 프로젝트 생성 및 메인 웰컴 페이지 구현
- [x] 백엔드 구현: `Book` 도메인, `MemoryBookRepository`, `BookServiceImpl`, `BookController` 구현
- [x] 화면 연동: 샘플 데이터를 포함한 **책 목록 조회 화면(`GET /books`)** 구현
- [x] 배포: `master` 브랜치 첫 Merge 및 `v1.0-list` 태그 발행

---

## 🛠️ 기술적 예외 처리 및 트래픽 기록 (Troubleshooting)

### 📌 2026.07.07 - 원격 저장소 이력 충돌
- **증상**: 첫 로컬 머지 후 푸시 과정에서 `rejected (fetch first)` 에러 발생.
- **원인**: GitHub 원격 생성 시 포함된 `README.md` 이력이 로컬에 동기화되지 않아 발생.
- **해결**: `git pull origin master --rebase` 명령어로 원격 이력을 로컬 베이스라인 위로 정렬한 후 푸시 성공.

### 📌 2026.07.09 - 타임리프 PropertyNotFoundException (화이트라벨 에러)
- **증상**: 상세 페이지 진입 시 화이트라벨 에러 발생.
- **원인**: 컨트롤러에서 `Optional<Book>` 객체를 알맹이 분리 없이 그대로 Model에 담아 뷰로 넘김으로써, 타임리프가 필드 추출에 실패함.
- **해결**: 백엔드 단에서 `.orElseThrow(() -> new IllegalArgumentException(...))`를 통해 `Book` 객체만 빼내어 Model에 담아주도록 수정.

### 📌 2026.08.06 - 검색어(keyword) 및 독서 상태(status) 필터 상호 유실 버그 (RCA)
- **증상**: 검색을 수행한 후 [완독] 등 독서 상태 필터 버튼을 클릭하면 `keyword` 파라미터가 날아가며 검색 결과가 초기화됨. 반대로 필터 적용 중 검색을 실행해도 필터 상태가 해제되는 현상 확인.
- **원인**:
    - HTML 내 상태 필터 버튼 링크가 `th:href="@{/books(status='DONE')}"` 형태로 되어 있어 현재 요청의 `keyword` 파라미터를 물고 가지 못함.
    - Controller에서 `keyword`를 파라미터로 받았으나 `model.addAttribute("keyword", keyword)`를 누락하여 새로고침 후 View의 검색창 인풋(`th:value="${keyword}"`)에 값이 전파되지 않음.
- **해결**:
    - Controller에서 `keyword`를 Model에 담아 View로 재전달.
    - View의 검색 Form에 `<input type="hidden" name="status" th:value="${status}">`를 배치하고, 상태 버튼 쿼리 스트링에 `keyword`를 함께 바인딩(`th:href="@{/books(status='DONE', keyword=${keyword})}"`)하여 검색 조건과 필터 조건이 동적으로 보존되도록 개선.
- **성과 및 배운 점**
  - 단순 기능 추가를 넘어, 사용자 동선(UX) 관점에서 쿼리 파라미터가 끊기지 않도록 설계하는 '상태 유지'의 중요성을 체감함.

### 📌 2026.08.20 - Validation DTO 적용 후 수정 페이지 500 오류
- **증상**: 수정 페이지 진입 시 `SpelEvaluationException` 발생.
- **원인**:
  - 기존 수정 폼에서는 `Book` 객체를 View에 전달하여 `${book.id}`를 사용하고 있었으나, Validation 적용 과정에서 View의 `book` 객체를 `BookForm`으로 변경함.
  - `BookForm`에는 `id` 필드가 존재하지 않아 Thymeleaf가 `${book.id}` 프로퍼티를 찾지 못함.
- **해결**:
  - 수정 폼의 입력 데이터는 `BookForm`으로 유지하고, 수정 대상 도서의 식별자는 URL의 `@PathVariable`로 관리하도록 역할 분리.
  - `th:field="*{...}"`는 `BookForm`의 필드에만 사용하도록 수정.
  - 기존 `book.id` 참조 코드를 제거하여 DTO와 View의 데이터 구조를 일치시킴.
- **성과 및 배운 점**
  - 검증용 DTO를 도입할 경우 기존 도메인 객체를 직접 참조하던 View 코드도 함께 변경해야 함을 확인.
  - DTO의 역할을 '사용자 입력값 전달 및 검증'으로 명확하게 분리하고, 도메인 객체와의 변환 시점을 Controller에서 관리하는 구조를 확립함.

### 📌 2026.08.20 - 선택형 평점의 null 값 처리 오류
- **증상**: 등록 과정에서 평점을 선택하지 않아 `rating = null`로 저장된 도서가 수정 화면에서 진입 시 강제로 선텍해야 하는 상황 발생.
- **원인**:
  - 수정 폼의 `<select>`에 `rating = null`과 대응되는 선택지가 존재하지 않아 브라우저가 첫 번째 옵션을 기본 선택함.
- **해결**:
  - `<option value="">평점을 선택하세요</option>`을 추가하여 `null` 상태를 명시적으로 표현하도록 수정.
  - `th:field="*{rating}"`를 통해 기존 평점이 존재하는 경우 해당 값이 자동으로 선택되도록 구성.
- **성과 및 배운 점**
  - Thymeleaf의 `th:field`를 활용하면서도 실제 HTML의 기본 선택 동작까지 고려해야 함을 학습함.