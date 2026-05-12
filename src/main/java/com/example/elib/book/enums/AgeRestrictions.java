package com.example.elib.book.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgeRestrictions {
    ZERO_PLUS("0+"),
    SIX_PLUS("6+"),
    TWELVE_PLUS("12+"),
    SIXTEEN_PLUS("16+"),
    EIGHTEEN_PLUS("18+");

    private final String label;
}