package com.example.elib.holder.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HolderType {
    SHELF("shelf"),
    CABINET("cabinet"),
    RACK("rack"),
    DEPOSITORY("depository"),
    DISPLAY("display"),
    LOCKER("locker"),
    TEMPORARY("temporary");

    private final String label;
}
