package com.example.elib.circulation.service;

import com.example.elib.booking.dto.response.BookingDto;
import com.example.elib.copy.dto.response.CopyDto;

import java.util.UUID;

public interface CirculationService {
    BookingDto reserveBook(UUID userId, UUID bookId);
    BookingDto reserveCopy(UUID userId, UUID copyId);
    BookingDto cancelReservation(UUID bookingId);
    BookingDto issueFromReservation(UUID bookingId);
    BookingDto issueDirect(UUID userId, UUID bookId);
    BookingDto issueDirectCopy(UUID userId, UUID copyId);
    BookingDto returnBook(UUID bookingId);
}