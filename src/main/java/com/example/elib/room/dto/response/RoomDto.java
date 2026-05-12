package com.example.elib.room.dto.response;

import lombok.*;

import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RoomDto {
    UUID id;
    String name;
}