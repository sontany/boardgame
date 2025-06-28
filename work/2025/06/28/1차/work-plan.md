# 상세 작업 계획서

## 1. 프로젝트 구조 및 설정

### 1.1 패키지 구조
```
src/main/kotlin/com/boardgame/
├── BoardgameApplication.kt
├── config/
│   ├── SwaggerConfig.kt
│   └── WebConfig.kt
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
│   │   ├── BoardGameRequest.kt
│   │   ├── PlayerRequest.kt
│   │   ├── GameSessionRequest.kt
│   │   └── ReviewRequest.kt
│   └── response/
│       ├── BoardGameResponse.kt
│       ├── PlayerResponse.kt
│       ├── GameSessionResponse.kt
│       └── ReviewResponse.kt
└── exception/
    ├── GlobalExceptionHandler.kt
    └── CustomExceptions.kt
```

### 1.2 테스트 구조
```
src/test/kotlin/com/boardgame/
├── controller/
│   ├── BoardGameControllerTest.kt
│   ├── PlayerControllerTest.kt
│   ├── GameSessionControllerTest.kt
│   └── ReviewControllerTest.kt
├── service/
│   ├── BoardGameServiceTest.kt
│   ├── PlayerServiceTest.kt
│   ├── GameSessionServiceTest.kt
│   └── ReviewServiceTest.kt
└── integration/
    └── BoardgameApplicationTests.kt
```

## 2. 도메인 모델 설계

### 2.1 BoardGame (보드게임)
```kotlin
data class BoardGame(
    val id: Long,
    val name: String,
    val description: String,
    val minPlayers: Int,
    val maxPlayers: Int,
    val playTime: Int, // 분 단위
    val difficulty: Int, // 1-5 난이도
    val category: String,
    val publisher: String,
    val releaseYear: Int,
    val rating: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
```

### 2.2 Player (플레이어)
```kotlin
data class Player(
    val id: Long,
    val nickname: String,
    val email: String,
    val level: Int,
    val totalGamesPlayed: Int,
    val favoriteCategory: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
```

### 2.3 GameSession (게임 세션)
```kotlin
data class GameSession(
    val id: Long,
    val boardGameId: Long,
    val playerIds: List<Long>,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val winnerId: Long?,
    val status: GameStatus, // WAITING, PLAYING, FINISHED
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

enum class GameStatus {
    WAITING, PLAYING, FINISHED
}
```

### 2.4 Review (리뷰)
```kotlin
data class Review(
    val id: Long,
    val boardGameId: Long,
    val playerId: Long,
    val rating: Int, // 1-5
    val comment: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
```

## 3. API 엔드포인트 설계

### 3.1 BoardGame API
- `GET /api/boardgames` - 보드게임 목록 조회
- `GET /api/boardgames/{id}` - 보드게임 상세 조회
- `POST /api/boardgames` - 보드게임 등록
- `PUT /api/boardgames/{id}` - 보드게임 수정
- `DELETE /api/boardgames/{id}` - 보드게임 삭제
- `GET /api/boardgames/search?category={category}` - 카테고리별 검색

### 3.2 Player API
- `GET /api/players` - 플레이어 목록 조회
- `GET /api/players/{id}` - 플레이어 상세 조회
- `POST /api/players` - 플레이어 등록
- `PUT /api/players/{id}` - 플레이어 정보 수정
- `DELETE /api/players/{id}` - 플레이어 삭제

### 3.3 GameSession API
- `GET /api/game-sessions` - 게임 세션 목록 조회
- `GET /api/game-sessions/{id}` - 게임 세션 상세 조회
- `POST /api/game-sessions` - 게임 세션 생성
- `PUT /api/game-sessions/{id}/start` - 게임 시작
- `PUT /api/game-sessions/{id}/finish` - 게임 종료
- `DELETE /api/game-sessions/{id}` - 게임 세션 삭제

### 3.4 Review API
- `GET /api/reviews` - 리뷰 목록 조회
- `GET /api/reviews/boardgame/{boardGameId}` - 특정 보드게임 리뷰 조회
- `POST /api/reviews` - 리뷰 작성
- `PUT /api/reviews/{id}` - 리뷰 수정
- `DELETE /api/reviews/{id}` - 리뷰 삭제

## 4. 의존성 설정 (build.gradle.kts)

### 4.1 주요 의존성
```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    
    // SQLite (향후 사용)
    runtimeOnly("org.xerial:sqlite-jdbc")
    
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
}
```

## 5. Mock Repository 구현 전략

### 5.1 메모리 기반 데이터 저장
- ConcurrentHashMap을 사용한 Thread-Safe 구현
- AtomicLong을 사용한 ID 자동 증가
- 초기 샘플 데이터 제공

### 5.2 Mock 데이터 예시
```kotlin
// BoardGame 샘플 데이터
val sampleBoardGames = listOf(
    BoardGame(1L, "카탄", "자원 수집 전략 게임", 3, 4, 90, 3, "전략", "코스모스", 1995, 4.5),
    BoardGame(2L, "스플렌더", "보석 상인 게임", 2, 4, 30, 2, "엔진빌딩", "스페이스 카우보이", 2014, 4.2),
    BoardGame(3L, "윙스팬", "조류 테마 엔진 빌딩", 1, 5, 70, 3, "엔진빌딩", "스톤마이어", 2019, 4.7)
)
```

## 6. 테스트 전략

### 6.1 단위 테스트
- Service 계층 로직 테스트
- Mock Repository를 사용한 격리된 테스트
- 각 메서드별 정상/예외 케이스 테스트

### 6.2 통합 테스트
- MockMvc를 사용한 Controller 테스트
- 전체 API 엔드포인트 테스트
- JSON 직렬화/역직렬화 테스트

### 6.3 테스트 커버리지 목표
- 서비스 계층: 90% 이상
- 컨트롤러 계층: 80% 이상

## 7. 구현 순서

### 7.1 1차 구현 (기본 구조)
1. 프로젝트 초기 설정 및 의존성 구성
2. 도메인 모델 및 DTO 클래스 작성
3. Exception 처리 클래스 작성
4. Swagger 설정

### 7.2 2차 구현 (핵심 기능)
1. Mock Repository 구현
2. Service 계층 구현
3. Controller 계층 구현
4. 기본 CRUD API 완성

### 7.3 3차 구현 (테스트 및 완성)
1. 단위 테스트 작성
2. 통합 테스트 작성
3. API 문서화 완성
4. 전체 기능 검증

## 8. 완료 기준
- [ ] 프로젝트가 정상적으로 실행됨
- [ ] Swagger UI에서 모든 API 테스트 가능
- [ ] 모든 테스트 코드 통과 (90% 이상 커버리지)
- [ ] 4개 도메인의 기본 CRUD 작업 완료
- [ ] Mock 데이터로 실제 API 동작 확인
- [ ] 예외 처리 및 검증 로직 구현 완료

## 9. 추가 고려사항
- 향후 SQLite 연동을 위한 확장 가능한 구조 유지
- RESTful API 설계 원칙 준수
- 적절한 HTTP 상태 코드 사용
- 입력 데이터 검증 및 예외 처리
- API 응답 형식 통일
