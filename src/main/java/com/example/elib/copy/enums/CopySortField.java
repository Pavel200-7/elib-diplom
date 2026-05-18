package com.example.elib.copy.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CopySortField {
    ISBN("isbn"),
    INVENTORY_NUMBER("inventoryNumber"),
    CREATED_AT("createdAt"),
    UPDATED_AT("updatedAt");

    private final String fieldName;
}
