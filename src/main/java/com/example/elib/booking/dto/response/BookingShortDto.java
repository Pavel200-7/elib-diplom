package com.example.elib.booking.dto.response;

import com.example.elib.booking.enums.BookingStatus;
import com.example.elib.copy.dto.response.CopyShortDto;
import com.example.elib.user.dto.response.UserDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BookingShortDto {
    UUID id;
    UUID userId;
    String userEmail;
    CopyShortDto copy;
    LocalDateTime started;
    LocalDateTime finishing;
    LocalDateTime finished;
    BookingStatus status;
}
