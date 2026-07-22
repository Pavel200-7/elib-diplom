package com.example.elib.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserSortField {
    READER_BOOK_NUMBER("readerBookNumber"),
    FIRST_NAME("firstName"),
    BIRTH_DATE("birthDate"),
    STAUS("status");

    private final String fieldName;
}