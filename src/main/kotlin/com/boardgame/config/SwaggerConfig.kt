package com.boardgame.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Swagger/OpenAPI 문서화를 위한 설정 클래스
 * 
 * API 문서화를 위한 Swagger UI 및 OpenAPI 스펙을 구성합니다.
 */
@Configuration
class SwaggerConfig {

    /**
     * OpenAPI 설정을 정의합니다.
     * 
     * @return API 문서화를 위한 OpenAPI 구성
     */
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(apiInfo())
            .servers(listOf(
                Server().url("http://localhost:8080").description("Local Development Server")
            ))
    }

    /**
     * API 정보를 정의합니다.
     * 
     * @return API 문서에 표시될 기본 정보
     */
    private fun apiInfo(): Info {
        return Info()
            .title("Yacht Dice Game API")
            .description("요트다이스 보드게임 API 문서")
            .version("v1.0.0")
            .contact(
                Contact()
                    .name("Boardgame API Support")
                    .email("support@boardgame.com")
            )
            .license(
                License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")
            )
    }
}
