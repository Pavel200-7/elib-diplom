package com.example.elib.book.dto.request;

import com.example.elib.book.enums.AgeRestrictions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdateBookDto {
    String name;
    UUID authorId;
    UUID genreId;
    UUID literatureGroupId;
    UUID publishingId;
    UUID languageId;
    Integer pages;
    Integer publicationYear;
    AgeRestrictions ageRestrictions;
    String description;
}