package com.example.elib.booking.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {
    ISSUED("issued"),
    CLOSED("closed");

    private final String label;
}
