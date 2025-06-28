package com.boardgame.integration

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = ["spring.main.web-application-type=reactive"])
class BoardgameApplicationTests {

    @Test
    fun contextLoads() {
        // Spring Boot 애플리케이션 컨텍스트가 정상적으로 로드되는지 테스트
    }
}
