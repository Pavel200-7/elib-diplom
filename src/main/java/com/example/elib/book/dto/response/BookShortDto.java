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
public class BookShortDto {
    UUID id;
    String name;
    String authorName;
    String genreName;
    Integer publicationYear;
    AgeRestrictions ageRestrictions;
}