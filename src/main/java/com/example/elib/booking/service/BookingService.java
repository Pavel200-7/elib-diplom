package com.example.elib.booking.service;

import com.example.elib.booking.dto.request.CreateBookingDto;
import com.example.elib.booking.dto.request.GetBookingCriteria;
import com.example.elib.booking.dto.response.BookingDto;
import com.example.elib.booking.dto.response.BookingShortDto;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    BookingDto makeIssue(CreateBookingDto dto);
    BookingDto makeReturning(UUID id);
    BookingDto getBooking(UUID id);
    List<BookingDto> getUserBookings(GetBookingCriteria criteria);
    List<BookingDto> getActiveUserBookings(UUID userId);
    List<BookingShortDto> getUserBookingsPage(GetBookingCriteria criteria);

}