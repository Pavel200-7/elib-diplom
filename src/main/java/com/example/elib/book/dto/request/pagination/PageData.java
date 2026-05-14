package com.example.elib.book.dto.request.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PageData {

    @Min(0)
    private int page;

    @Min(0)
    @Max(100)
    private int size;
}
