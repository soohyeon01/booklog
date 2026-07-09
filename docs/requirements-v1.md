# 📚 북로그(BookLog) v1.0 기능 명세서 & 개발 일지

## 📋 1. 기능 및 데이터 명세 (Spec)

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
- **2026.07.07 (Tue)**
    - [x] 초기 세팅: 프로젝트 생성 및 메인 웰컴 페이지 구현
    - [x] 백엔드 구현: `Book` 도메인, `MemoryBookRepository`, `BookServiceImpl`, `BookController` 구현
    - [x] 1: 화면 연동: 샘플 데이터를 포함한 **책 목록 조회 화면(`GET /books`)** 구현
    - [x] 배포: `master` 브랜치 첫 Merge 및 `v1.0-list` 태그 발행


- **2026.07.09 (Thu)**
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

---

## 💡 개발 특이사항 및 리팩터링 기록 (Refactoring Notes)

### 🛠️ 에러 및 트래픽 기록
- 2026.07.07
  - 첫 로컬 머지 후 푸시 과정에서 rejected (fetch first) 에러 발생. 원격 저장소의 README.md 이력이 로컬에 동기화되지 않아 발생한 문제로 확인되어, git pull origin master --rebase 명령어로 이력을 정렬한 후 푸시 성공함.

  - books.html: 컨트롤러에서 넘어온 List<Book>을 타임리프 th:each 반복문으로 처리함. 평점 속성은 별 특수문자가 반복 출력되도록 구현함.
