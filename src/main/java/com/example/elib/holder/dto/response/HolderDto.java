package com.example.elib.holder.dto.response;

import com.example.elib.holder.enums.HolderType;
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
public class HolderDto {
    UUID id;
    String name;
    UUID roomId;
    String roomName;
    HolderType type;
}