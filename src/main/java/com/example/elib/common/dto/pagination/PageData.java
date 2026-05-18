package com.example.elib.common.dto.pagination;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PageData {

    @Min(0)
    private Integer page;

    @Min(0)
    @Max(100)
    private Integer size;
}
