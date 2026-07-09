# 📚 북로그(BookLog) v1.2 스펙 명세서 & 개발 일지

## 📋 1. 스펙 명세

### 1-1. 도메인 모델 (Book)
* **ID**: 고유 식별 번호 (`Long`)
* **제목**: 책 이름 (**필수**, `String`)
* **저자**: 글쓴이 (`String`)
* **독서 상태**: `BookStatus` (WISH: 읽고 싶은, READING: 읽는 중, DONE: 완독)
* **평점**: 별점 (1~5, `Integer`)
* **한줄평**: 소감 (`String`)
* **메모**: 독서 회고 (`String`)

### 1-2. 핵심 기능 요구사항
* **기능 1**: 책 목록 조회 (`GET /books`) - 최신 등록 순 정렬
* **기능 2**: 새 책 등록 (`GET/POST /books/add`) - PRG 패턴 적용
* **기능 3**: 책 상세 조회 (`GET /books/{bookId}`)
* **기능 4**: 책 정보 수정 (`GET/POST /books/{bookId}/edit`)
* **기능 5**: 책 삭제 (`POST /books/{bookId}/delete`)

---

## 🗺️ 2. 개발 일지 (Daily Log)

### 🟡 예정된 기능 (To-Do) -> 🟢 모두 완료 (Done)
### 2026.07.07 (Tue)
  - [x] 초기 세팅: 프로젝트 생성 및 메인 웰컴 페이지 구현
  - [x] 백엔드 구현: `Book` 도메인, `MemoryBookRepository`, `BookServiceImpl`, `BookController` 구현
  - [x] 화면 연동: 샘플 데이터를 포함한 **책 목록 조회 화면(`GET /books`)** 구현
  - [x] 배포: `master` 브랜치 첫 Merge 및 `v1.0-list` 태그 발행


### 2026.07.09 (Thu)
- [v1.0-final] 순수 CRUD 및 PRG 패턴 확립
  - [x] `@ModelAttribute`를 활용한 데이터 자동 바인딩 및 저장 로직 구현
  - [x] 새로고침 중복 등록 버그 방지를 위한 PRG(Post-Redirect-Get) 패턴 도입
  - [x] `@PathVariable` 및 `Optional.orElseThrow()` 기반 안전한 상세 조회 로직 구현
  - [x] 상세 페이지 내 도서 수정 및 삭제 기능 연동 완료

- [v1.1] UX 고도화 및 데이터 흐름 최적화
  - [x] `addFlashAttribute`도입: 세션 기반 일회성 알림 메시지 배달로 주소창 최적화 (?status=true 제거)
  - [x] `books.html`에서 제목 클릭 시 상세 페이지로 바로 이동하는 하이퍼링크(<a>) 연동
  - [x] 메인 목록 화면 내 행별 [삭제] 버튼 및 자바스크립트 confirm 얼럿창 추가

- [v1.2-final] 아키텍처 리팩터링
  - [x] URL 인코딩 방어: RedirectAttributes.addAttribute()의 템플릿 치환 방식을 채택하여 보안성 강화
  - [x] 타임리프 프래그먼트(th:fragment) 도입: header/footer 등 중복 UI 구조를 base.html로 모듈화하여 코드 가독성 및 유지보수성 향상
  - [x] 중복 UX 해결: 공통 헤더에서 목록 이동 링크를 제거하고, 화면별 목적에 맞는 네비게이션 버튼을 본문에 배치
  - [x] 스트림 필터링: 메인 화면 상단에 상태별 도서 권수 카운팅 대시보드 배치 및 ?status=READING 쿼리 파라미터 동적 필터링 구현
  - [x] 예외 커스텀 페이지 구축: 잘못된 접근 시 톰캣 화이트라벨 에러 페이지 대신 error/404.html 및 error/500.html 커스텀 화면 노출

---

## 🛠️ 기술적 예외 처리 및 트래픽 기록 (Trouble Shooting)
### 📌 2026.07.07 - 원격 저장소 이력 충돌
- 첫 로컬 머지 후 푸시 과정에서 rejected (fetch first) 에러 발생.
- GitHub 원격 생성 시 포함된 README.md 이력이 로컬에 동기화되지 않아 발생.
- git pull origin master --rebase 명령어로 원격 이력을 로컬 베이스라인 위로 정렬한 후 푸시 성공.

### 📌 2026.07.09 - 타임리프 PropertyNotFoundException (화이트라벨 에러)
- 상세 페이지 진입 시 화이트라벨 에러 발생.
- 컨트롤러에서 Optional<Book> 객체를 알맹이 분리 없이 그대로 Model에 담아 뷰로 넘김으로써, 타임리프가 필드 추출에 실패함.
- 백엔드 단에서 .orElseThrow(() -> new IllegalArgumentException(...))를 통해 Book 객체만 빼내어 Model에 담아주도록 수정.

---

### 🔗 관련 문서 바로가기
* [🖼️ v1.2 결과 화면(Screenshots) 보러가기](./screenshots.md)
* [🏠 메인 README로 돌아가기](../../README.md)