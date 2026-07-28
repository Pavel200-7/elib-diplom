package com.example.elib.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sequences {
    COPIES_COUNTER("copies_counter"),
    READERS_COUNTER("readers_counter");

    private final String name;
}
