package com.boardgame.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("보드게임 서비스 API")
                    .description("보드게임 관련 서비스를 위한 RESTful API")
                    .version("v1.0.0")
                    .contact(
                        Contact()
                            .name("보드게임 서비스 팀")
                            .email("boardgame@example.com")
                    )
            )
    }
}
