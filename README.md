# 프로젝트 이름 (추후 정의)

> Kotlin + Spring Boot 기반 클린 아키텍처 템플릿 프로젝트

도메인 주제는 아직 미정이며, 이 프로젝트는 계층 구조, 의존성 원칙, 테스트 환경, 설정 등
일관된 개발을 위한 템플릿을 구성합니다.

---

## 🧱 프로젝트 구조
```plantetxt
src/main/kotlin/com/boardgame/
├── controller      # API 요청을 받는 Web 계층
├── application     # 유스케이스 계층 (비즈니스 흐름)
├── domain          # 핵심 도메인 모델 및 규칙, 포트 정의
├── infra           # 외부 구현체 (DB, API 등)
└── config          # 설정 클래스 (Swagger, DB 등)
```

---

## 🔁 계층 간 의존 관계

- `controller` → `application`
- `application` → `domain`
- `infra` → `domain`
- `config`는 전체 접근 가능
- `domain`은 외부에 전혀 의존하지 않음

---

## 💡 개발 스타일

- Kotlin 언어 기반, Spring Boot 프레임워크 사용
- 모든 public 클래스 및 함수는 `KDoc`을 사용해 문서화
- Entity 생성을 위한 팩토리는 `companion object` 사용
- 패키지 구조는 도메인 단위로 세분화 (`domain.user`, `application.user` 등)
- 포트(인터페이스)를 통해만 외부 계층과 통신 (Port & Adapter 구조)

---

## 🧪 테스트 구성

- 테스트 프레임워크: [Kotest](https://kotest.io/)
- 테스트 DB: H2 (in-memory)
- 실제 운영 DB: SQLite
- 테스트에서는 SpringBootTest 또는 Kotest의 테스트 컨텍스트 사용

```bash
./gradlew test