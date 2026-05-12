package com.example.elib.publishing.dto.response;

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
public class PublishingDto {
    UUID id;
    String name;
    String description;
    UUID countryId;
    String countryName;
}