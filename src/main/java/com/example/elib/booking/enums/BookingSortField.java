package com.example.elib.booking.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingSortField {
    STARTED("started");

    private final String name;
}
