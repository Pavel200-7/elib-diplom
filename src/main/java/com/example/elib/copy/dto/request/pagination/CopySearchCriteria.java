package com.example.elib.copy.dto.request.pagination;

import com.example.elib.book.enums.AgeRestrictions;
import com.example.elib.copy.enums.CopyStatus;
import lombok.*;

import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CopySearchCriteria {
    String inventoryNumber;
    String isbn;

    UUID holderId;

    UUID bookId;
    CopyStatus status;
}