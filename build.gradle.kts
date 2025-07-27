import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.5.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    kotlin("plugin.jpa") version "2.0.0"
}

group = "com.boardgame"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starter Web (웹 애플리케이션 개발에 필수)
    implementation("org.springframework.boot:spring-boot-starter-web")
    
    // Spring Boot Starter Data JPA (데이터베이스 연동)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    
    // Kotlin Reflect (코틀린 리플렉션 지원)
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    
    // Kotlin Stdlib JVM (코틀린 표준 라이브러리)
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    
    // SQLite JDBC Driver (운영용 DB)
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
    
    // Swagger/OpenAPI for API Documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")
    
    // H2 Database (개발 및 테스트용 인메모리 DB)
    runtimeOnly("com.h2database:h2")
    
    // Spring Boot DevTools (개발 편의성 증진)
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    
    // Spring Boot Configuration Processor (YAML/properties 파일 자동 완성)
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Test Dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    
    // Kotest Framework
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest:kotest-property:5.8.0")
    testImplementation("io.kotest:kotest-extensions-spring:4.4.3")
    
    // MockK for Kotlin-friendly mocking
    testImplementation("io.mockk:mockk:1.13.9")
    
    // H2 Database for Testing
    testRuntimeOnly("com.h2database:h2")
}

// Kotlin 컴파일러 설정
tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.set(listOf("-Xjsr305=strict")) // JSR-305 어노테이션에 대한 엄격한 검사
        jvmTarget.set(JvmTarget.JVM_21) // JVM 타겟 버전 설정
    }
}

// 테스트 설정
tasks.withType<Test> {
    useJUnitPlatform() // JUnit 플랫폼 사용 (Kotest는 JUnit 플랫폼 위에서 동작)
}

// Spring Boot 빌드 설정
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("boardgame-application.jar") // 빌드된 JAR 파일 이름 설정
}

// Kotlin 컴파일러 설정
tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.set(listOf("-Xjsr305=strict")) // JSR-305 어노테이션에 대한 엄격한 검사
        jvmTarget.set(JvmTarget.JVM_21) // JVM 타겟 버전 설정
    }
}

// Spring Boot 빌드 설정
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("your-spring-boot-application.jar") // 빌드된 JAR 파일 이름 설정
}

// 테스트 설정
tasks.test {
    useJUnitPlatform() // JUnit 5 사용
}

// 프론트엔드 빌드 태스크
tasks.register<Exec>("buildFrontend") {
    group = "build"
    description = "Build React frontend"
    workingDir = file("frontend")
    
    // OS에 따른 명령어 설정
    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        commandLine("cmd", "/c", "npm", "run", "build")
    } else {
        commandLine("npm", "run", "build")
    }
}

// 프론트엔드 빌드 파일을 static 폴더로 복사
tasks.register<Copy>("copyFrontendBuild") {
    group = "build"
    description = "Copy frontend build to static resources"
    dependsOn("buildFrontend")
    
    from("frontend/dist")
    into("src/main/resources/static")
}

// processResources 태스크가 copyFrontendBuild에 의존하도록 설정
tasks.named("processResources") {
    dependsOn("copyFrontendBuild")
}
