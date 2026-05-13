package com.example.elib.copy.dto.request;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SetRegularHolderDto {
    private UUID holderId;
    private List<UUID> copiesId;
}
