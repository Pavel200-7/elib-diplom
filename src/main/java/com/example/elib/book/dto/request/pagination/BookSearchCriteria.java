package com.example.elib.book.dto.request.pagination;

import com.example.elib.book.enums.AgeRestrictions;
import lombok.*;

import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BookSearchCriteria {
    String name;

    UUID authorId;
    UUID genreId;
    UUID literatureGroupId;
    UUID publishingId;
    UUID languageId;

    Integer pagesMin;
    Integer pagesMax;
    Integer publicationYearMin;
    Integer publicationYearMax;

    AgeRestrictions ageRestrictions;
}