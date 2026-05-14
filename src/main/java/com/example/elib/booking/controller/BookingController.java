package com.example.elib.booking.controller;

import com.example.elib.booking.dto.request.GetBookingCriteria;
import com.example.elib.booking.dto.response.BookingDto;
import com.example.elib.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBooking(@PathVariable UUID id) {
        BookingDto booking = bookingService.getBooking(id);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/user")
    public ResponseEntity<List<BookingDto>> getUserBookings(@RequestBody GetBookingCriteria criteria) {
        List<BookingDto> bookings = bookingService.getUserBookings(criteria);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<BookingDto>> getActiveUserBookings(@PathVariable UUID userId) {
        List<BookingDto> bookings = bookingService.getActiveUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }


}