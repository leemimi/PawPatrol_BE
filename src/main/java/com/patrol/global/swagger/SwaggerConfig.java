package com.patrol.global.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        String securitySchemeName = "accessToken";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

        Components components = new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name("JSESSIONID")              // 쿠키 이름
                        .type(SecurityScheme.Type.APIKEY) // API Key 방식
                        .in(SecurityScheme.In.COOKIE)    // 쿠키에 위치
                        .description("인증 쿠키"));        // 설명

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}