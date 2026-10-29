# 📚 북로그(BookLog) v3.4 스펙 명세서 & 개발 일지

## 📋 스펙 명세

> v3.4는 평문으로 저장되던 비밀번호를 **BCrypt 해시로 전환**하고, 시드 계정 없이도 사용할 수 있도록 **회원가입**을 추가한 보안 강화 버전입니다.

### 1. 비밀번호 해시 저장

* **알고리즘**: BCrypt (`BCryptPasswordEncoder`, 솔트 자동 포함)
* **의존성**: `spring-security-crypto`만 추가하고 `spring-boot-starter-security`는 도입하지 않음 — 기존 인터셉터 기반 인증을 유지하면서 암호화 도구만 사용
* **로그인 비교**: `equals`가 아닌 `matches` 사용 (같은 비밀번호도 솔트에 따라 해시가 달라지기 때문)
* **로그인 실패 응답**: 아이디 없음과 비밀번호 불일치를 구분하지 않고 동일하게 응답하여 계정 존재 여부 노출을 방지
* **세션 고정 공격 방지**: 로그인 성공 시 `changeSessionId()`로 세션 ID 교체
* **기존 평문 계정 마이그레이션**: 1회성 `CommandLineRunner`로 BCrypt 형식이 아닌 비밀번호만 해시로 변환하여 재시작 시 이중 해싱 방지 (멱등)
* **시드 계정**: `TestDataInit`에서 생성하는 계정도 해시로 저장

### 2. 회원가입 (`GET/POST /signup`)

* **입력 검증**
    * 아이디: 영문·숫자 4~20자
    * 비밀번호: 공백 없는 ASCII 8~16자
    * 비밀번호 확인 일치, 이름 최대 50자
* **권한 고정**: 권한은 폼에서 받지 않고 항상 `USER`로 저장하여 권한 상승 방지
* **중복 아이디 이중 방어**: 사전 조회(`existsByLoginId`)에 더해, 동시 가입으로 DB `UNIQUE` 제약에 걸린 경우(`DataIntegrityViolationException`)도 같은 메시지로 처리
    * 예외는 서비스 트랜잭션 안에서 삼키지 않고 컨트롤러에서 처리 (삼키면 롤백 표시로 인해 `UnexpectedRollbackException` 발생 가능)
* **흐름**: 가입 성공 시 로그인 화면으로 Redirect하며 안내 메시지 표시, `/signup`은 인터셉터 범위 밖이라 비로그인 접근 가능

### 3. 테스트

* `MemberServiceTest`(Mockito): 해시 저장 및 `USER` 권한 고정, 중복 아이디 예외 및 미저장, 인증 성공/실패 (실제 BCrypt를 낮은 강도로 사용)

### 4. 한계 및 후속 후보

* CSRF 방어 없음(직접 구현한 세션 인증의 한계), 로그인 시도 횟수 제한 없음, 비밀번호 변경·찾기 미구현

---

## 🗺️ 개발 일지

### 2026.10.22 (Thu)
- [v3.4-security] 비밀번호 해시 저장
    - [x] `spring-security-crypto` 의존성 및 `PasswordEncoder`(BCrypt) 빈 등록
    - [x] 로그인 비교를 `matches`로 설정, 로그인 시 세션 ID 교체
    - [x] 기존 평문 계정을 BCrypt로 변환하는 1회성 마이그레이션 구현 (형식 판별로 이중 해싱 방지), 시드 계정도 해시로 저장

- [v3.4-signup] 회원가입
    - [x] `SignupForm` 검증, `MemberService.signup`, `SignupController`, `signupForm.html` 구현, 로그인 화면에 가입 링크와 완료 메시지 추가
    - [x] 중복 아이디 이중 방어(사전 조회 + DB UNIQUE 위반), 권한 `USER` 고정
    - [x] `MemberServiceTest` 작성

---

### 🔗 관련 문서 바로가기
* [📝 v3.3 스펙 명세서 보러가기](requirements-v3.3.md)
* [🏠 메인 README로 돌아가기](../../README.md)