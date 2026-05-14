package com.example.elib.auth.service;


import com.example.elib.auth.dto.request.LogoutRequestDto;
import com.example.elib.auth.dto.request.RefreshTokenRequestDto;
import com.example.elib.auth.dto.response.LoginResponseDto;
import reactor.core.publisher.Mono;

public interface KeycloakAuthService {
    String buildAuthorizationUrl();
    Mono<LoginResponseDto> exchangeCodeForTokens(String code);  // теперь возвращает LoginResponseDto
    Mono<LoginResponseDto> refreshToken(RefreshTokenRequestDto request);
    Mono<Void> logout(LogoutRequestDto request);
}