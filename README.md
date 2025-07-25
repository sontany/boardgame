#Kotlin Spring Boot
> Kotlin + Spring Boot 기반 보드게임 프로젝트

이 프로젝트는 Kotlin Spring Boot 애플리케이션입니다.
계층 구조, 의존성 원칙, 테스트 환경, 설정 등 일관된 개발을 위한 기본 구조를 제공합니다.

## 🧱 프로젝트 구조

```
src/main/kotlin/com/boardgame/
├── controller/     # API 요청을 받는 Web 계층
├── application/    # 서비스 계층 (비즈니스 흐름)
├── infra/          # 외부 구현체 (DB, API 등)
│   ├── entity/ # JPA 엔티티
│   └── repository/ # Spring Data JPA 저장소
└── config/         # 설정 클래스 (Swagger, DB 등)
```

## 🔁 계층 간 의존 관계

- `controller` → `application`
- `infra` → `application`
- `application`은 전체적인 비지니스 처리
- `config`는 전체 접근 가능

## 💡 개발 스타일

- Kotlin 언어 기반, Spring Boot 프레임워크 사용
- 모든 public 클래스 및 함수는 `KDoc`을 사용해 문서화
- Entity 생성을 위한 팩토리는 필요시 `companion object` 사용
- 패키지 구조는 어플리케이션에서 모두 처리 (`application.player`,`application.dice` 등)
- 어플리케이션에서 엔티티와 레포지토리를 의존해서 개발

## 🧪 테스트 구성

- 테스트 프레임워크: [Kotest](https://kotest.io/)
- 테스트 DB: H2 (in-memory)
- 실제 운영 DB: SQLite
- 테스트 스타일: `DescribeSpec`

## 📝 API 문서화

- Swagger/OpenAPI 3.0을 사용한 API 문서 자동화
- 접속 URL: `/api/swagger-ui.html`

## 🚀 시작하기

### 필수 조건

- JDK 21 이상
- Gradle 8.5 이상

### 빌드 및 실행

```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun

# 테스트 실행
./gradlew test
```

## 🔧 기술 스택

- Kotlin 2.0.0
- Spring Boot 3.5.3
- Spring Data JPA
- SQLite (운영)
- H2 Database (테스트)
- Kotest
- MockK
- Swagger/OpenAPI 3.0
