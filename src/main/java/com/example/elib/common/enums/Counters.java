package com.example.elib.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Counters {
    COPIES_COUNTER("copies_counter");

    private final String name;
}
