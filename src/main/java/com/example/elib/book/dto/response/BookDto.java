package com.example.elib.book.dto.response;

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
public class BookDto {
    UUID id;
    String name;
    UUID authorId;
    String authorName;
    UUID genreId;
    String genreName;
    UUID literatureGroupId;
    String literatureGroupName;
    UUID publishingId;
    String publishingName;
    UUID languageId;
    String languageName;
    Integer pages;
    Integer publicationYear;
    AgeRestrictions ageRestrictions;
    String description;
}