package com.example.elib.book.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookSortField {
    NAME("name"),
    PUBLICATION_YEAR("publicationYear"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt");

    private final String name;
}