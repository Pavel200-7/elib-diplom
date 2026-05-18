package com.example.elib.copy.dto.response;

import com.example.elib.book.dto.response.BookShortDto;
import com.example.elib.copy.enums.CopyStatus;
import com.example.elib.holder.dto.response.HolderDto;
import lombok.*;

import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CopyShortDto {
    private UUID id;
    private CopyStatus status;

    private String inventoryNumber;
    private String isbn;

    private BookShortDto book;
    private HolderDto holder;
}
