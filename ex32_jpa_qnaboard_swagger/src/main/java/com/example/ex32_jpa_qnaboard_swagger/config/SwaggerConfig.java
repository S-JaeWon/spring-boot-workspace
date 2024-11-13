package com.example.ex32_jpa_qnaboard_swagger.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
    // http://localhost:8080/swagger-ui/index.html
    // JWT
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
    @Bean
    public OpenAPI openAPI() {
        Server server = new Server(); // API 서버 설정
        server.setUrl("/");
        Info info = new Info()
                .title("QnA Board API") // API 문서 제목
                .version("v1.0.0") // API 문서 버전
                .description("This is QnA Board API"); // API 문서 설명
        // JWT 관련 추가
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components().addSecuritySchemes("JWT", createAPIKeyScheme()))
                .info(info)
                .servers(List.of(server));
    }
}