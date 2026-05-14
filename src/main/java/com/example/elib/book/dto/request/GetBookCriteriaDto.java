package com.example.elib.book.dto.request;

import com.example.elib.book.dto.request.pagination.BookSearchCriteria;
import com.example.elib.book.dto.request.pagination.BookSortCriteria;
import com.example.elib.book.dto.request.pagination.PageData;
import jakarta.validation.Valid;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetBookCriteriaDto {
    BookSearchCriteria searchCriteria;
    BookSortCriteria sortCriteria;

    @Valid
    PageData pageData;
}
