package com.example.elib.common.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoles {
    USER("USER"),
    ADMIN("ADMIN");

    private final String name;
}
