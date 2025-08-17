package com.ebook.userservice.filter;

import com.ebook.userservice.service.JwtService;
import com.ebook.userservice.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    public static final String BEARER = "Bearer ";
    private final JwtService jwtService;
    private final HandlerExceptionResolver resolver;
    private final UserService userService;

    public AuthorizationFilter(JwtService jwtService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver, UserService userService) {
        this.jwtService = jwtService;
        this.resolver = resolver;
        this.userService = userService;
    }

    private void authenticate(HttpServletRequest req) {
        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER)) {
            String jwt = header.substring(BEARER.length()).trim();
            if (this.jwtService.validateJwtToken(jwt)) {
                String mail = this.jwtService.getEmailFromJwtToken(jwt);
                UserDetails userDetails = this.userService.loadUserByUsername(mail);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        try {
            this.authenticate(req);
        } catch (RuntimeException ex) {
            this.resolver.resolveException(req, res, null, ex);
            return;
        }
        filterChain.doFilter(req, res);
    }
}