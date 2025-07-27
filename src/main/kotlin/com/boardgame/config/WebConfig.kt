package com.boardgame.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver

/**
 * Spring Boot 웹 설정
 * - 정적 파일 서빙 설정
 * - SPA 라우팅 지원 (React Router)
 */
@Configuration
class WebConfig : WebMvcConfigurer {

    /**
     * 정적 리소스 핸들러 설정
     * React 빌드 파일들을 서빙하고 SPA 라우팅을 지원합니다.
     */
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // API 경로가 아닌 모든 요청을 정적 파일로 처리
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(object : PathResourceResolver() {
                override fun getResource(resourcePath: String, location: Resource): Resource? {
                    val requestedResource = location.createRelative(resourcePath)
                    
                    // 요청된 파일이 존재하면 반환
                    if (requestedResource.exists() && requestedResource.isReadable) {
                        return requestedResource
                    }
                    
                    // API 요청이면 null 반환 (404 처리)
                    if (resourcePath.startsWith("api/")) {
                        return null
                    }
                    
                    // SPA 라우팅: 파일이 없으면 index.html 반환
                    return ClassPathResource("/static/index.html")
                }
            })
    }
}
