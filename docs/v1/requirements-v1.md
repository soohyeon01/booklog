<<<<<<< HEAD
<<<<<<< HEAD:docs/requirements-v1.md
<<<<<<< HEAD
# 📑 v1.0 요구사항 명세서

### 1. 도메인 모델 (Book)
* **ID**: 시스템이 발급하는 고유 식별 번호 (`Long`)
* **제목**: 책 이름 (**필수 입력**, `String`)
* **저자**: 글쓴이 (`String`)
* **독서 상태**: `BookStatus` (WISH, READING, DONE)
* **평점**: 1점부터 5점까지의 별점 (`Integer`)
* **한줄평**: 가벼운 소감 (`String`)
* **메모**: 길게 남기는 독서 회고 (`String`)

### 2. v1.0 구현 기능
* [ ] [Issue #2] Book 도메인 및 BookStatus Enum 생성
* [ ] [Issue #3] MemoryBookRepository 구현 (CRUD 저장소)
* [ ] [Issue #4] BookService 구현 (비즈니스 로직)
=======
# 📚 북로그(BookLog) v1.0 기능 명세서 & 개발 일지
=======
# 📚 북로그(BookLog) v1.2 스펙 명세서 & 개발 일지
>>>>>>> f236086 (feat: 모든 페이지 css 추가 작업 완료):docs/v1/requirements-v1.md
=======
# 📚 북로그(BookLog) v1.3 스펙 명세서 & 개발 일지
>>>>>>> 61eb56b (docs: v1.3 스펙 명세, 개발 일지 분리 및 수정)

* **버전**: v1.3
* **목적**: 검색어(제목/저자) 기반 도서 검색 기능 추가 및 기존 v1.2 독서 상태 필터와의 유기적 파라미터 연동을 통한 UX 고도화.

---

## 📋 스펙 명세

### 1. 도메인 모델 (Book)
* **ID**: 고유 식별 번호 (`Long`)
* **제목**: 책 이름 (**필수**, `String`)
* **저자**: 글쓴이 (`String`)
* **독서 상태**: `BookStatus` (WISH: 읽고 싶은, READING: 읽는 중, DONE: 완독)
* **평점**: 별점 (1~5, `Integer`)
* **한줄평**: 소감 (`String`)
* **메모**: 독서 회고 (`String`)

### 1-1. 핵심 기능 요구사항
* **기능 1**: 책 목록 조회 (`GET /books`) - 최신 등록 순 정렬
* **기능 2**: 새 책 등록 (`GET/POST /books/add`) - PRG 패턴 적용
* **기능 3**: 책 상세 조회 (`GET /books/{bookId}`)
* **기능 4**: 책 정보 수정 (`GET/POST /books/{bookId}/edit`)
* **기능 5**: 책 삭제 (`POST /books/{bookId}/delete`)

### v1.3 주요 요구사항 및 구현 스펙
### 1. 복합 도서 검색 기능 (Search & Filter)
* **검색 대상**: 도서 제목(`title`) 및 저자(`author`)
* **검색 조건**:
  * 대소문자 구분이 없는 대소문자 무시(Case-Insensitive) 검색 지원 (`toLowerCase()`).
  * `keyword`와 `status`가 동시에 들어올 경우 두 조건을 모두 만족하는 교집합 데이터 반환.
  * 키워드가 없을 경우 전체 목록 또는 상태 필터링 결과 반환.

<<<<<<< HEAD
## 🗺️ 2. 개발 일지 (Daily Log)

### 🟡 예정된 기능 (To-Do) -> 🟢 모두 완료 (Done)
### 2026.07.07 (Tue)
  - [x] 초기 세팅: 프로젝트 생성 및 메인 웰컴 페이지 구현
  - [x] 백엔드 구현: `Book` 도메인, `MemoryBookRepository`, `BookServiceImpl`, `BookController` 구현
  - [x] 화면 연동: 샘플 데이터를 포함한 **책 목록 조회 화면(`GET /books`)** 구현
  - [x] 배포: `master` 브랜치 첫 Merge 및 `v1.0-list` 태그 발행


<<<<<<< HEAD
- **2026.07.09 (Thu)**
<<<<<<< HEAD
  - [x] 새 책 등록 폼 화면 구현 (`addForm.html`) 및 GET 매핑 연동
  - [x] `@ModelAttribute`를 활용한 데이터 바인딩 및 저장 로직 구현 (POST)
  - [x] 새로고침 중복 등록 버그 방지를 위한 **PRG 패턴** 및 `RedirectAttributes` 적용 완료

### 🟡 예정된 기능 (To-Do)
- **Next Step**
    - [ ] 기능 3: 책 상세 조회 화면 구현
    - [ ] 기능 4, 5: 책 수정 및 삭제 기능 구현 
  
=======
  - [x] 2: 새 책 등록 폼 화면 구현 (`addForm.html`) 및 GET 매핑 연동
  - [x] 2: `@ModelAttribute`를 활용한 데이터 바인딩 및 저장 로직 구현 (POST)
  - [x] 2: 새로고침 중복 등록 버그 방지를 위한 **PRG 패턴** 및 `RedirectAttributes` 적용 완료
  - [x] 3: `@PathVariable`을 활용한 책 상세 조회 기능 구현
  - [x] 4, 5: 책 수정 및 삭제 기능 구현
  - [x] 배포: v1.0 최종 완료 태그 발행 (`v1.0-final`)
  
- v1.1 수정 사항
  - [x] `addFlashAttribute` 도입으로 등록/수정 완료 알림 메시지 세분화 및 주소창 최적화
  - [x] `books.html` 목록에서 제목 클릭 시 상세 페이지로 다이렉트 이동 구현
  - [x] `books.html` 목록 내 즉시 삭제 버튼 추가 및 백엔드 POST 연동 완료
=======
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
>>>>>>> 5d1991c (feat: v1.2 기능 개선 완료 (중복 UX 해결, 필터링, 예외 커스텀 페이지))

>>>>>>> 717938e (feat: v1.0 도서 CRUD 기능 구현 및 테스트 완료 (조회/등록/상세/수정/삭제))
---

## 🛠️ 기술적 예외 처리 및 트래픽 기록 (Trouble Shooting)
### 📌 2026.07.07 - 원격 저장소 이력 충돌
- 첫 로컬 머지 후 푸시 과정에서 rejected (fetch first) 에러 발생.
- GitHub 원격 생성 시 포함된 README.md 이력이 로컬에 동기화되지 않아 발생.
- git pull origin master --rebase 명령어로 원격 이력을 로컬 베이스라인 위로 정렬한 후 푸시 성공.

<<<<<<< HEAD
<<<<<<< HEAD
- **2026.07.07:**
    - 첫 로컬 머지 후 푸시 과정에서 rejected (fetch first) 에러 발생.<br>
      원격 저장소의 README.md 이력이 로컬에 동기화되지 않아 발생한 문제로 확인되어,<br>
      git pull origin master --rebase 명령어로 이력을 정렬한 후 푸시 성공함.
  
    - books.html: 컨트롤러에서 넘어온 List<Book>을 타임리프 th:each 반복문으로 처리함. 평점 속성은 별 특수문자가 반복 출력되도록 구현함.
>>>>>>> refs/rewritten/docs-v1-0-기능-명세서-개발-일지-개발-특이사항-추가
=======
### 🛠️ 에러 및 트래픽 기록
- 2026.07.07
  - 첫 로컬 머지 후 푸시 과정에서 rejected (fetch first) 에러 발생. 원격 저장소의 README.md 이력이 로컬에 동기화되지 않아 발생한 문제로 확인되어, git pull origin master --rebase 명령어로 이력을 정렬한 후 푸시 성공함.

  - books.html: 컨트롤러에서 넘어온 List<Book>을 타임리프 th:each 반복문으로 처리함. 평점 속성은 별 특수문자가 반복 출력되도록 구현함.
<<<<<<< HEAD

### 🚀 향후 개선 및 추가 구현 아이디어 (Backlog)
- 알림 메시지 세분화 (book.html)
  - 현재 단순 param.status 조건문으로만 처리 중인 안내 문구를, 추후 넘어오는 파라미터 값에 따라 [등록 완료] / [수정 완료] 메시지로 명확히 분기 처리할 예정.

- 목록 내 빠른 삭제 기능 추가 (books.html)
  - 상세 페이지에 들어가지 않고도 메인 목록 화면의 평점 옆 칸에서 즉시 도서를 삭제할 수 있도록, 삭제 버튼 열을 추가하고 POST 매핑 연동 예정.
>>>>>>> d0e89d4 (docs: v1.0 향후 구현 아이디어 추가)
=======
>>>>>>> 2a68079 (feat: v1.1 기능 개선 완료 (목록 내 삭제, 제목 링크 , FlashAttribute 적용))
=======
### 📌 2026.07.09 - 타임리프 PropertyNotFoundException (화이트라벨 에러)
- 상세 페이지 진입 시 화이트라벨 에러 발생.
- 컨트롤러에서 Optional<Book> 객체를 알맹이 분리 없이 그대로 Model에 담아 뷰로 넘김으로써, 타임리프가 필드 추출에 실패함.
- 백엔드 단에서 .orElseThrow(() -> new IllegalArgumentException(...))를 통해 Book 객체만 빼내어 Model에 담아주도록 수정.
<<<<<<< HEAD:docs/requirements-v1.md
>>>>>>> 5d1991c (feat: v1.2 기능 개선 완료 (중복 UX 해결, 필터링, 예외 커스텀 페이지))
=======
=======
### 2. 파라미터 상태 유지 (Parameter State Persistence)
* **검색 폼 (`<form>`)**: 검색 버튼 클릭 시 현재 선택된 `status` 값이 `hidden` 파라미터로 함께 전송되어 필터링 상태 유지.
* **상태 버튼 그룹 (`<a href="...">`)**: 독서 상태(전체/WISH/READING/DONE) 변경 시 현재 입력된 `keyword` 값이 쿼리 스트링으로 함께 전달되어 검색 상태 유지.
* **Controller-Model 바인딩**: `model.addAttribute("keyword", keyword)`를 통해 검색 후에도 Input 폼에 검색어가 그대로 노출되도록 보장.
>>>>>>> 61eb56b (docs: v1.3 스펙 명세, 개발 일지 분리 및 수정)

---

### 🔗 관련 문서 바로가기
* [🖼️ v1.2 결과 화면(Screenshots) 보러가기](./screenshots.md)
* [🏠 메인 README로 돌아가기](../../README.md)
>>>>>>> f236086 (feat: 모든 페이지 css 추가 작업 완료):docs/v1/requirements-v1.md
