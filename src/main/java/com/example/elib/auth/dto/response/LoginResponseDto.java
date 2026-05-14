package com.example.elib.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Integer expiresIn;
    private String tokenType;
}