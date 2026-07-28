package com.example.elib.common.dto.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

/**
 * Номер страницы и количество позиций на одной странице
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageData {

    @Min(0)
    @Builder.Default
    private Integer page = 0;

    @Min(0)
    @Max(100)
    @Builder.Default
    private Integer size = 20;
}
