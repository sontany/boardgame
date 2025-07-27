#Kotlin Spring Boot + React
> Kotlin + Spring Boot + React 기반 요트다이스 보드게임 프로젝트

이 프로젝트는 Kotlin Spring Boot 백엔드와 React 프론트엔드가 통합된 풀스택 애플리케이션입니다.
계층 구조, 의존성 원칙, 테스트 환경, 설정 등 일관된 개발을 위한 기본 구조를 제공합니다.

## 🎮 게임 소개
**요트 다이스 (Yacht Dice)** - 5개의 주사위를 사용하여 13개의 카테고리에 점수를 기록하는 클래식 주사위 게임

### 게임 규칙
- 각 턴마다 최대 3번까지 주사위를 굴릴 수 있습니다
- 원하는 주사위만 선택하여 다시 굴릴 수 있습니다
- 13개의 점수 카테고리 중 하나를 선택하여 점수를 기록합니다
- 상위 섹션(1~6)에서 63점 이상 획득 시 35점 보너스를 받습니다
- 모든 카테고리를 채우면 게임이 종료됩니다

## 🧱 프로젝트 구조

```
├── src/main/kotlin/com/boardgame/     # 백엔드 (Spring Boot)
│   ├── controller/                    # API 요청을 받는 Web 계층
│   ├── application/                   # 서비스 계층 (비즈니스 흐름)
│   ├── infra/                        # 외부 구현체 (DB, API 등)
│   │   ├── entity/                   # JPA 엔티티
│   │   └── repository/               # Spring Data JPA 저장소
│   └── config/                       # 설정 클래스 (Swagger, DB, Web 등)
├── frontend/                         # 프론트엔드 (React)
│   ├── src/
│   │   ├── components/               # 재사용 가능한 컴포넌트
│   │   ├── pages/                    # 페이지 컴포넌트
│   │   ├── services/                 # API 서비스
│   │   └── types/                    # TypeScript 타입 정의
│   └── dist/                         # 빌드된 정적 파일
└── src/main/resources/static/        # 프론트엔드 빌드 파일 (자동 복사)
```

## 🚀 **통합 실행 방법 (추천)**

### 한 번의 명령어로 전체 애플리케이션 실행
```bash
# 프론트엔드 자동 빌드 + 백엔드 실행
./gradlew bootRun

# 또는 빌드 후 실행
./gradlew build
./gradlew bootRun
```

### 접속 URL
- **메인 애플리케이션**: http://localhost:8080
- **API 문서 (Swagger)**: http://localhost:8080/swagger-ui.html
- **H2 데이터베이스 콘솔**: http://localhost:8080/h2-console

## 🔁 계층 간 의존 관계

- `controller` → `application`
- `infra` → `application`
- `application`은 전체적인 비지니스 처리
- `config`는 전체 접근 가능

## 💡 개발 스타일

### 백엔드 (Kotlin + Spring Boot)
- Kotlin 언어 기반, Spring Boot 프레임워크 사용
- 모든 public 클래스 및 함수는 `KDoc`을 사용해 문서화
- Entity 생성을 위한 팩토리는 필요시 `companion object` 사용
- 패키지 구조는 어플리케이션에서 모두 처리 (`application.player`,`application.dice` 등)
- 어플리케이션에서 엔티티와 레포지토리를 의존해서 개발

### 프론트엔드 (React + TypeScript)
- React 18 + TypeScript + Tailwind CSS
- 컴포넌트 기반 모듈화 설계
- React Hooks를 활용한 상태 관리
- Axios를 통한 백엔드 API 연동

## 🧪 테스트 구성

- 테스트 프레임워크: [Kotest](https://kotest.io/)
- 테스트 DB: H2 (in-memory)
- 실제 운영 DB: H2 (개발용)
- 테스트 스타일: `DescribeSpec`

## 📝 API 문서화

- Swagger/OpenAPI 3.0을 사용한 API 문서 자동화
- 접속 URL: http://localhost:8080/swagger-ui.html

## 🛠️ 개발 환경 설정

### 필수 조건
- JDK 21 이상
- Node.js 18 이상
- npm 또는 yarn

### 개발 서버 실행 (분리 모드)
```bash
# 백엔드만 실행 (포트 8080)
./gradlew bootRun

# 프론트엔드만 실행 (포트 3000) - 별도 터미널
cd frontend
npm run dev
```

### 빌드
```bash
# 전체 빌드 (프론트엔드 + 백엔드)
./gradlew build

# 프론트엔드만 빌드
cd frontend
npm run build

# 백엔드만 빌드
./gradlew build -x buildFrontend
```

### 테스트 실행
```bash
# 백엔드 테스트
./gradlew test

# 프론트엔드 테스트 (향후 추가 예정)
cd frontend
npm test
```

## 🔧 기술 스택

### 백엔드
- Kotlin 2.0.0
- Spring Boot 3.5.3
- Spring Data JPA
- H2 Database
- Kotest + MockK
- Swagger/OpenAPI 3.0

### 프론트엔드
- React 18 + TypeScript
- Tailwind CSS
- Vite (빌드 도구)
- Axios (HTTP 클라이언트)
- React Router (라우팅)

## 🎯 주요 기능

### 게임 플레이
- ✅ 게임 생성 및 플레이어 등록
- ✅ 주사위 굴리기 (최대 3회, 선택적 재굴리기)
- ✅ 13개 점수 카테고리 지원
- ✅ 실시간 점수 계산 및 미리보기
- ✅ 보너스 점수 자동 계산
- ✅ 게임 완료 및 결과 분석

### UI/UX
- ✅ 반응형 디자인 (모바일/태블릿/데스크톱)
- ✅ 직관적인 주사위 인터페이스
- ✅ 실시간 게임 상태 표시
- ✅ 애니메이션 효과
- ✅ 사용자 친화적 에러 처리

## 📁 빌드 결과물

```
src/main/resources/static/     # 프론트엔드 빌드 파일 (자동 생성)
├── index.html                 # React 앱 진입점
├── assets/
│   ├── index.css             # 스타일시트
│   └── index.js              # JavaScript 번들
└── ...
```

## 🚀 배포

### JAR 파일 생성
```bash
./gradlew build
# 결과: build/libs/boardgame-application.jar
```

### 실행
```bash
java -jar build/libs/boardgame-application.jar
# http://localhost:8080 에서 전체 애플리케이션 접근 가능
```

## 🔄 개발 워크플로우

1. **백엔드 개발**: `src/main/kotlin/` 에서 API 개발
2. **프론트엔드 개발**: `frontend/src/` 에서 UI 개발
3. **통합 테스트**: `./gradlew bootRun` 으로 전체 애플리케이션 실행
4. **빌드**: `./gradlew build` 로 프로덕션 빌드
5. **배포**: 생성된 JAR 파일 배포

---

**🎲 즐거운 요트다이스 게임을 즐겨보세요!**
