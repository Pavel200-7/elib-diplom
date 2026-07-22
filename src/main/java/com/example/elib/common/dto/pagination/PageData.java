package com.example.elib.common.dto.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * Номер страницы и количество позиций на одной странице
 */
@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class PageData {

    @Min(0)
    private Integer page = 0;

    @Min(0)
    @Max(100)
    private Integer size = 20;
}
