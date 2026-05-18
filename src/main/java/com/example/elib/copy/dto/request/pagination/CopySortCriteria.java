package com.example.elib.copy.dto.request.pagination;

import com.example.elib.copy.enums.CopySortField;
import lombok.*;
import org.springframework.data.domain.Sort;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CopySortCriteria {
    CopySortField sortBy;
    Sort.Direction sortDirection;
}
