package com.example.elib.copy.dto.response;

import com.example.elib.copy.enums.CopyStatus;
import lombok.*;

import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CopyDto {
    private UUID id;
    private UUID holderId;
    private String inventoryNumber;
    private String isbn;
    private UUID bookId;
    private CopyStatus status;
}
