package com.example.elib.copy.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CopyStatus {
    ADDED("added"),
    AVAILABLE("available"),
    IN_TRANSIT("in_transit"),
    RESERVED("reserved"),
    ISSUED("issued"),
    WRITTEN_OFF("written_off");

    private final String label;
}
