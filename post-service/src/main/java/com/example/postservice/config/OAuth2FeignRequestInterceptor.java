package com.example.postservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer";

    @Override
    public void apply(RequestTemplate template) {
        // Получаем текущий аутентификационный объект из контекста безопасности
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getToken() != null) {
            // Получаем токен и добавляем его в заголовок запроса
            String token = authentication.getToken().getTokenValue();
            template.header(AUTHORIZATION_HEADER, String.format("%s %s", TOKEN_TYPE, token));
        }
    }
}