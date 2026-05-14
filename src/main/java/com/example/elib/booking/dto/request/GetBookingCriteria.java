package com.example.elib.booking.dto.request;

import com.example.elib.booking.enums.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetBookingCriteria {
    BookingStatus status;
    UUID userId;
    UUID copyId;
    LocalDateTime createdFrom;
    LocalDateTime createdTo;
    Boolean overdueOnly;
}
