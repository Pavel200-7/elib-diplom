package com.example.elib.book.dto.request.pagination;

import com.example.elib.book.enums.SortField;
import lombok.*;
import org.springframework.data.domain.Sort;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BookSortCriteria {
    SortField sortBy;
    Sort.Direction sortDirection;
}
