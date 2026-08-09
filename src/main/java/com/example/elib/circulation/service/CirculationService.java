package com.example.elib.circulation.service;

import com.example.elib.booking.dto.response.BookingDto;

import java.util.UUID;

public interface CirculationService {
    BookingDto issueCopy(UUID userId, UUID copyId);
    BookingDto returnBook(UUID bookingId);
}