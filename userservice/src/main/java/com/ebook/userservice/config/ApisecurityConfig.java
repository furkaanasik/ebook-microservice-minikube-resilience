package com.ebook.userservice.config;

import com.ebook.userservice.filter.AuthorizationFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class ApisecurityConfig {

    private final HandlerExceptionResolver resolver;

    private static final String[] SWAGGER_ENDPOINTS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/swagger-resources",
            "/api-docs/**"
    };


    public ApisecurityConfig(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ObjectProvider<AuthorizationFilter> authTokenFilter) throws Exception {
        http.cors(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(cfg -> cfg
                //H2
                .requestMatchers("/h2-console/**").permitAll()
                // Swagger
                .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                // User
                .requestMatchers(HttpMethod.POST , "/api/v1/user/login").permitAll()
                .requestMatchers(HttpMethod.POST , "/api/v1/user").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                // OTHER
                .anyRequest().authenticated()
        );

        http.formLogin(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        http.exceptionHandling(handling -> handling
                .authenticationEntryPoint((request, response, authException) -> resolver.resolveException(request, response, null, authException))
        );

        http.addFilterBefore(authTokenFilter.getObject(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
