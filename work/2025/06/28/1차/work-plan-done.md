# 작업 내역서

## 작업 개요
보드게임 서비스를 위한 Kotlin/Spring Boot 기반 프로젝트 초기 구조 구성 완료

## 작업 진행 내역

### 1. 프로젝트 초기 설정
#### 1.1 Git 브랜치 생성
```bash
git checkout -b feature-work-plan-1차-보드게임서비스
```

#### 1.2 빌드 설정 파일 생성
- **파일**: `build.gradle.kts`
- **내용**: Kotlin, Spring Boot, Swagger, 테스트 의존성 구성
- **주요 의존성**:
  - Spring Boot Starter Web
  - Spring Boot Starter Validation
  - Jackson Kotlin Module
  - SpringDoc OpenAPI (Swagger)
  - Mockito Kotlin (테스트)

#### 1.3 애플리케이션 설정
- **파일**: `src/main/kotlin/com/boardgame/BoardgameApplication.kt`
- **파일**: `src/main/resources/application.yml`
- **내용**: 서버 포트 8080, Swagger UI 경로 설정

### 2. 도메인 모델 구현
#### 2.1 핵심 도메인 엔티티 생성
- **BoardGame**: 보드게임 정보 (이름, 설명, 플레이어 수, 난이도 등)
- **Player**: 플레이어 정보 (닉네임, 이메일, 레벨, 선호 카테고리 등)
- **GameSession**: 게임 세션 (보드게임, 참가자, 상태, 승자 등)
- **Review**: 리뷰 (보드게임, 작성자, 평점, 내용 등)

#### 2.2 열거형 정의
- **GameStatus**: WAITING, PLAYING, FINISHED

### 3. DTO 클래스 구현
#### 3.1 Request DTO
- **BoardGameRequest**: 보드게임 생성/수정 요청
- **PlayerRequest**: 플레이어 생성/수정 요청
- **GameSessionRequest**: 게임 세션 생성 요청
- **ReviewRequest**: 리뷰 생성/수정 요청

#### 3.2 Response DTO
- **BoardGameResponse**: 보드게임 응답
- **PlayerResponse**: 플레이어 응답
- **GameSessionResponse**: 게임 세션 응답
- **ReviewResponse**: 리뷰 응답

#### 3.3 검증 어노테이션 적용
- `@NotBlank`, `@NotNull`, `@Min`, `@Max`, `@Email`, `@Size` 등 적용

### 4. 예외 처리 구현
#### 4.1 커스텀 예외 클래스
- **BoardGameNotFoundException**: 보드게임 미발견
- **PlayerNotFoundException**: 플레이어 미발견
- **GameSessionNotFoundException**: 게임 세션 미발견
- **ReviewNotFoundException**: 리뷰 미발견
- **InvalidGameSessionStateException**: 잘못된 게임 세션 상태
- **DuplicateEmailException**: 중복 이메일

#### 4.2 글로벌 예외 핸들러
- **GlobalExceptionHandler**: 모든 예외를 통합 처리
- **ErrorResponse**: 통일된 에러 응답 형식

### 5. Swagger 설정
#### 5.1 설정 클래스
- **SwaggerConfig**: OpenAPI 3.0 설정
- **API 문서 정보**: 제목, 설명, 버전, 연락처 정보

### 6. Mock Repository 구현
#### 6.1 메모리 기반 데이터 저장소
- **ConcurrentHashMap** 사용으로 Thread-Safe 구현
- **AtomicLong**으로 ID 자동 증가
- **초기 샘플 데이터** 제공

#### 6.2 Repository 클래스
- **BoardGameRepository**: 보드게임 CRUD 및 카테고리 검색
- **PlayerRepository**: 플레이어 CRUD 및 이메일 중복 체크
- **GameSessionRepository**: 게임 세션 CRUD 및 상태별 조회
- **ReviewRepository**: 리뷰 CRUD 및 보드게임/플레이어별 조회

### 7. Service 계층 구현
#### 7.1 비즈니스 로직 구현
- **BoardGameService**: 보드게임 관리 로직
- **PlayerService**: 플레이어 관리 로직 (이메일 중복 검증 포함)
- **GameSessionService**: 게임 세션 관리 로직 (상태 변경 검증 포함)
- **ReviewService**: 리뷰 관리 로직 (참조 무결성 검증 포함)

#### 7.2 예외 처리 및 검증
- 존재하지 않는 엔티티 접근시 적절한 예외 발생
- 비즈니스 규칙 위반시 검증 로직 적용

### 8. Controller 계층 구현
#### 8.1 RESTful API 엔드포인트
- **BoardGameController**: 보드게임 CRUD API
- **PlayerController**: 플레이어 CRUD API
- **GameSessionController**: 게임 세션 관리 API
- **ReviewController**: 리뷰 CRUD API

#### 8.2 API 문서화
- **Swagger 어노테이션** 적용 (`@Tag`, `@Operation`, `@Parameter`)
- **HTTP 상태 코드** 적절히 사용 (200, 201, 204, 400, 404, 409, 500)

### 9. 테스트 코드 구현
#### 9.1 단위 테스트
- **BoardGameServiceTest**: 서비스 계층 로직 테스트
- **Mockito** 사용한 의존성 모킹

#### 9.2 통합 테스트
- **BoardGameControllerTest**: MockMvc를 사용한 API 테스트
- **BoardgameApplicationTests**: 애플리케이션 컨텍스트 로드 테스트

### 10. 프로젝트 구조 완성
#### 10.1 최종 패키지 구조
```
src/main/kotlin/com/boardgame/
├── BoardgameApplication.kt
├── config/
│   └── SwaggerConfig.kt
├── controller/
│   ├── BoardGameController.kt
│   ├── PlayerController.kt
│   ├── GameSessionController.kt
│   └── ReviewController.kt
├── service/
│   ├── BoardGameService.kt
│   ├── PlayerService.kt
│   ├── GameSessionService.kt
│   └── ReviewService.kt
├── repository/
│   ├── BoardGameRepository.kt
│   ├── PlayerRepository.kt
│   ├── GameSessionRepository.kt
│   └── ReviewRepository.kt
├── domain/
│   ├── BoardGame.kt
│   ├── Player.kt
│   ├── GameSession.kt
│   └── Review.kt
├── dto/
│   ├── request/
│   └── response/
└── exception/
    ├── GlobalExceptionHandler.kt
    └── CustomExceptions.kt
```

## 구현된 API 엔드포인트

### 보드게임 API
- `GET /api/boardgames` - 보드게임 목록 조회
- `GET /api/boardgames/{id}` - 보드게임 상세 조회
- `GET /api/boardgames/search?category={category}` - 카테고리별 검색
- `POST /api/boardgames` - 보드게임 등록
- `PUT /api/boardgames/{id}` - 보드게임 수정
- `DELETE /api/boardgames/{id}` - 보드게임 삭제

### 플레이어 API
- `GET /api/players` - 플레이어 목록 조회
- `GET /api/players/{id}` - 플레이어 상세 조회
- `POST /api/players` - 플레이어 등록
- `PUT /api/players/{id}` - 플레이어 정보 수정
- `DELETE /api/players/{id}` - 플레이어 삭제

### 게임 세션 API
- `GET /api/game-sessions` - 게임 세션 목록 조회
- `GET /api/game-sessions/{id}` - 게임 세션 상세 조회
- `POST /api/game-sessions` - 게임 세션 생성
- `PUT /api/game-sessions/{id}/start` - 게임 시작
- `PUT /api/game-sessions/{id}/finish` - 게임 종료
- `DELETE /api/game-sessions/{id}` - 게임 세션 삭제

### 리뷰 API
- `GET /api/reviews` - 리뷰 목록 조회
- `GET /api/reviews/{id}` - 리뷰 상세 조회
- `GET /api/reviews/boardgame/{boardGameId}` - 보드게임별 리뷰 조회
- `GET /api/reviews/player/{playerId}` - 플레이어별 리뷰 조회
- `POST /api/reviews` - 리뷰 작성
- `PUT /api/reviews/{id}` - 리뷰 수정
- `DELETE /api/reviews/{id}` - 리뷰 삭제

## 초기 샘플 데이터

### 보드게임 (5개)
1. 카탄 - 자원 수집 전략 게임
2. 스플렌더 - 보석 상인 게임
3. 윙스팬 - 조류 테마 엔진 빌딩
4. 아줄 - 타일 배치 게임
5. 티켓 투 라이드 - 기차 노선 연결 게임

### 플레이어 (5명)
1. 게임마스터 - 레벨 10, 전략 게임 선호
2. 보드게임러버 - 레벨 7, 가족게임 선호
3. 전략왕 - 레벨 8, 전략 게임 선호
4. 캐주얼플레이어 - 레벨 3, 파티게임 선호
5. 프로게이머 - 레벨 15, 엔진빌딩 선호

### 게임 세션 (3개)
1. 카탄 게임 - 완료된 세션
2. 스플렌더 게임 - 진행 중인 세션
3. 윙스팬 게임 - 대기 중인 세션

### 리뷰 (5개)
- 각 보드게임에 대한 다양한 평점과 리뷰 내용

## 기술적 특징

### 1. 확장 가능한 아키텍처
- 계층형 아키텍처 (Controller-Service-Repository)
- 의존성 주입을 통한 느슨한 결합
- 인터페이스 기반 설계로 향후 DB 연동 용이

### 2. 데이터 검증
- Bean Validation 사용한 입력 데이터 검증
- 비즈니스 규칙 검증 (이메일 중복, 게임 세션 상태 등)

### 3. 예외 처리
- 계층별 적절한 예외 처리
- 통일된 에러 응답 형식
- 사용자 친화적인 에러 메시지

### 4. API 문서화
- Swagger/OpenAPI 3.0 자동 문서 생성
- 상세한 API 설명 및 파라미터 정보

### 5. 테스트 코드
- 단위 테스트 및 통합 테스트
- Mock 객체를 활용한 격리된 테스트

## 완료 기준 달성 여부

- [x] 프로젝트가 정상적으로 실행됨
- [x] Swagger UI에서 모든 API 테스트 가능
- [x] 4개 도메인의 기본 CRUD 작업 완료
- [x] Mock 데이터로 실제 API 동작 확인
- [x] 예외 처리 및 검증 로직 구현 완료
- [x] 테스트 코드 작성 완료
- [x] RESTful API 설계 원칙 준수
- [x] 향후 SQLite 연동을 위한 확장 가능한 구조 유지

## 실행 방법

### 1. 애플리케이션 실행
```bash
./gradlew bootRun
```

### 2. Swagger UI 접속
```
http://localhost:8080/swagger-ui.html
```

### 3. API 문서 확인
```
http://localhost:8080/api-docs
```

### 4. 테스트 실행
```bash
./gradlew test
```

## 향후 확장 계획

### 1. 데이터베이스 연동
- SQLite 연동을 위한 JPA/Hibernate 설정
- Repository 인터페이스를 JpaRepository로 변경

### 2. 보안 기능
- Spring Security 적용
- JWT 기반 인증/인가

### 3. 추가 기능
- 파일 업로드 (보드게임 이미지)
- 검색 기능 강화
- 페이징 및 정렬

### 4. 성능 최적화
- 캐싱 적용
- 데이터베이스 인덱스 최적화

## 롤백 가능한 부분

### 1. Git 브랜치 롤백
```bash
git checkout main
git branch -D feature-work-plan-1차-보드게임서비스
```

### 2. 파일 삭제
- 전체 프로젝트 폴더 삭제로 완전 롤백 가능
- 개별 파일 단위 롤백도 가능

### 3. 의존성 롤백
- build.gradle.kts 파일 수정으로 의존성 제거 가능

## 작업 완료 시점
- **작업 시작**: 2025년 6월 28일
- **작업 완료**: 2025년 6월 28일
- **총 소요 시간**: 약 2시간
- **작업 상태**: 완료

## 최종 검증 결과
- 모든 계획된 기능이 정상적으로 구현됨
- API 엔드포인트가 예상대로 동작함
- 테스트 코드가 통과함
- Swagger 문서가 정상적으로 생성됨
- 확장 가능한 구조로 설계됨
