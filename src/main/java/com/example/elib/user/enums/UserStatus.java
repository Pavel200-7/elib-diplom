package com.example.elib.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    CREATED("created"),
    ACTIVATED("activated");

    private final String label;
}
