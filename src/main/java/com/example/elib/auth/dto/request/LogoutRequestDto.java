package com.example.elib.auth.dto.request;

import lombok.Data;

@Data
public class LogoutRequestDto {
    private String refreshToken;
}