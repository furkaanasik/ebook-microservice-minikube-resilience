package com.ebook.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.ServletContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {
    private static final String SCHEME_AUTH = "AuthorizationScheme";
    private static final List<String> GLOBAL_SCOPE = List.of("global");

    @Bean
    public OpenAPI openAPI(ServletContext context) {
        return new OpenAPI()
                .addServersItem(new Server()
                        .url(context.getContextPath())
                        .description("This Server"))
                .info(new Info()
                        .version("1.0.0")
                        .title("UserService API")
                        .description("UserService API"))
                .components(new Components()
                        .securitySchemes(securitySchemes()))
                .addSecurityItem(securityRequirements());
    }

    @Bean
    public WebMvcConfigurer forwardToIndex() {
        return new WebMvcConfigurer() {
            @SuppressWarnings("NullableProblems")
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html");
                registry.addRedirectViewController("/swagger-ui/", "/swagger-ui/index.html");
            }
        };
    }


    private Map<String, SecurityScheme> securitySchemes() {
        Map<String, SecurityScheme> schs = new LinkedHashMap<>();
        schs.put(SCHEME_AUTH, new SecurityScheme()
                .name(HttpHeaders.AUTHORIZATION)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));
        return schs;
    }

    private SecurityRequirement securityRequirements() {
        SecurityRequirement sr = new SecurityRequirement();
        sr.put(SCHEME_AUTH, GLOBAL_SCOPE);
        return sr;
    }
}
