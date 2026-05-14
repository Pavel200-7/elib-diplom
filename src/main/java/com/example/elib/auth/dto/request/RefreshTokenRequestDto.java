package com.example.elib.auth.dto.request;

import lombok.Data;

@Data
public class RefreshTokenRequestDto {
    private String refreshToken;
}