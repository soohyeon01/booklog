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