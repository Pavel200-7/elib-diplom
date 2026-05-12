package com.example.elib.booking.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {
    RESERVED("reserved"),
    ISSUED("issued"),
    CLOSED("closed"),
    CANCELLED("cancelled");

    private final String label;
}
