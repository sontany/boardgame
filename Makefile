.PHONY: dev build clean install

# 개발 서버 실행 (백엔드 + 프론트엔드)
dev:
	@echo "🚀 Starting development servers..."
	@make -j2 dev-backend dev-frontend

# 백엔드만 실행
dev-backend:
	@echo "🔧 Starting backend server..."
	./gradlew bootRun

# 프론트엔드만 실행  
dev-frontend:
	@echo "🎨 Starting frontend server..."
	cd frontend && npm run dev

# 빌드
build:
	@echo "🏗️ Building application..."
	./gradlew build
	cd frontend && npm run build

# 의존성 설치
install:
	@echo "📦 Installing dependencies..."
	cd frontend && npm install

# 정리
clean:
	@echo "🧹 Cleaning build files..."
	./gradlew clean
	cd frontend && rm -rf dist node_modules/.cache

# 전체 설정
setup: install
	@echo "✅ Setup complete!"

# 도움말
help:
	@echo "Available commands:"
	@echo "  make dev      - Start both backend and frontend"
	@echo "  make build    - Build both applications"
	@echo "  make install  - Install dependencies"
	@echo "  make clean    - Clean build files"
	@echo "  make setup    - Initial setup"
