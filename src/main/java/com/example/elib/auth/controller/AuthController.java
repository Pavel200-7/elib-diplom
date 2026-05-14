package com.example.elib.auth.controller;


import com.example.elib.auth.dto.request.LogoutRequestDto;
import com.example.elib.auth.dto.request.RefreshTokenRequestDto;
import com.example.elib.auth.dto.response.LoginResponseDto;
import com.example.elib.auth.service.KeycloakAuthService;
import com.example.elib.auth.utils.JwtHelper;
import com.example.elib.common.config.security.SecurityConfigProperties;
import com.example.elib.user.dto.request.CreateUserDto;
import com.example.elib.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;
    private final UserService userService;
    private final SecurityConfigProperties securityConfig;
    private final JwtHelper jwtHelper;

    @GetMapping("/authorize")
    public ResponseEntity<Void> authorize() {
        String authUrl = keycloakAuthService.buildAuthorizationUrl();
        log.info("Redirecting to Keycloak login: {}", authUrl);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(authUrl))
                .build();
    }

    @GetMapping("/callback")
    public Mono<ResponseEntity<Void>> callback(@RequestParam("code") String code) {
        log.info("Received authorization code: {}", code);

        return keycloakAuthService.exchangeCodeForTokens(code)
                .flatMap(loginResponse -> {
                    String accessToken = loginResponse.getAccessToken();
                    String refreshToken = loginResponse.getRefreshToken();

                    CreateUserDto createUserDto = jwtHelper.extractUserFromToken(accessToken);
                    try {
                        userService.createUser(createUserDto);
                        log.info("User created/retrieved successfully with id: {}", createUserDto.getId());
                    } catch (Exception e) {
                        log.warn("Failed to create/get user with id {}: {}", createUserDto.getId(), e.getMessage());
                    }

                    log.info(securityConfig.getFrontendRedirectUri());
                    String redirectWithTokens = securityConfig.getFrontendRedirectUri() +
                            "#access_token=" + accessToken +
                            "&refresh_token=" + refreshToken;

                    log.info(redirectWithTokens);

                    log.info("Successfully authenticated, redirecting to frontend");
                    return Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create(redirectWithTokens))
                            .<Void>build());
                })
                .onErrorResume(e -> {
                    log.error("Failed to exchange code for tokens: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.FOUND)
                            .location(URI.create(securityConfig.getFrontendRedirectUri() + "?error=auth_failed"))
                            .<Void>build());
                });
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<LoginResponseDto>> refresh(@RequestBody RefreshTokenRequestDto request) {
        return keycloakAuthService.refreshToken(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Failed to refresh token: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(@RequestBody LogoutRequestDto request) {
        return keycloakAuthService.logout(request)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> {
                    log.error("Logout error: {}", e.getMessage());
                    return Mono.just(ResponseEntity.ok().build());
                });
    }
}