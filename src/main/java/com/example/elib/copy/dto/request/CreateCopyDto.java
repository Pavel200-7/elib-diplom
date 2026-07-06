package com.example.elib.copy.dto.request;

import com.example.elib.book.entity.Book;
import com.example.elib.copy.enums.CopyStatus;
import com.example.elib.holder.entity.Holder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateCopyDto {
    @NotBlank
    private String isbn;
    private UUID bookId;
}
