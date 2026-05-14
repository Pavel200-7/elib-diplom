package com.example.elib.booking.dto.request;

import com.example.elib.copy.dto.response.CopyDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateBookingDto {
    UUID userId;
    UUID copyId;
}
