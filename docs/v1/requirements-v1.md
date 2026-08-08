# 📚 북로그(BookLog) v1.3 스펙 명세서 & 개발 일지

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
#### 1. 복합 도서 검색 기능 (Search & Filter)
* **검색 대상**: 도서 제목(`title`) 및 저자(`author`)
* **검색 조건**:
  * 대소문자 구분이 없는 대소문자 무시(Case-Insensitive) 검색 지원 (`toLowerCase()`).
  * `keyword`와 `status`가 동시에 들어올 경우 두 조건을 모두 만족하는 교집합 데이터 반환.
  * 키워드가 없을 경우 전체 목록 또는 상태 필터링 결과 반환.

#### 2. 동적 도서 정렬 기능 (Sorting)
* **정렬 옵션 지원**:
  * ⏱️ 최신순 (`id_desc`, 기본값)
  * ⏳ 오래된순 (`id_asc`)
  * 🔤 제목 내림차순 (`title_desc`) / 오름차순 (`title_asc`)
  * ⭐ 평점 높은순 (`rating_desc`) / 낮은순 (`rating_asc`)
* **안정성 (Null-Safety)**:
  * `Comparator.nullsLast()`를 적용하여 데이터 내 `null` 값이 존재하더라도 `NullPointerException` 없이 안전하게 최하단으로 배치 처리.
* **상태 유지 연동**:
  * 검색어(`keyword`) 및 필터(`status`) 상태와 정렬 파라미터(`sort`)가 서로 유실되지 않고 유기적으로 조합되어 조회 결과 반환.
* **사용자 UX 개선**:
  * 정렬 드롭다운(`<select>`) 선택 즉시 JavaScript(`changeSort`)를 통해 Form을 전송하여 별도의 [검색] 버튼 클릭 없이 정렬 반영.

---

### 🔗 관련 문서 바로가기
* [🖼️ v1.2 결과 화면(Screenshots) 보러가기](./screenshots.md)
* [🏠 메인 README로 돌아가기](../../README.md)
