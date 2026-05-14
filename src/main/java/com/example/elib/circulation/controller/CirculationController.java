package com.example.elib.circulation.controller;

import com.example.elib.booking.dto.response.BookingDto;
import com.example.elib.circulation.service.CirculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/circulation")
@RequiredArgsConstructor
public class CirculationController {

    private final CirculationService circulationService;

    @PostMapping("/reserve/book")
    public ResponseEntity<BookingDto> reserveBook(
            @RequestParam UUID userId,
            @RequestParam UUID bookId
    ) {
        BookingDto booking = circulationService.reserveBook(userId, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @PostMapping("/reserve/copy")
    public ResponseEntity<BookingDto> reserveCopy(
            @RequestParam UUID userId,
            @RequestParam UUID copyId
    ) {
        BookingDto booking = circulationService.reserveCopy(userId, copyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @PutMapping("/reservation/{bookingId}")
    public ResponseEntity<BookingDto> cancelReservation(@PathVariable UUID bookingId) {
        BookingDto booking = circulationService.cancelReservation(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/issue/from-reservation/{bookingId}")
    public ResponseEntity<BookingDto> issueFromReservation(@PathVariable UUID bookingId) {
        BookingDto booking = circulationService.issueFromReservation(bookingId);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/issue/direct/book")
    public ResponseEntity<BookingDto> issueDirect(
            @RequestParam UUID userId,
            @RequestParam UUID bookId
    ) {
        BookingDto booking = circulationService.issueDirect(userId, bookId);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/issue/direct/copy")
    public ResponseEntity<BookingDto> issueDirectCopy(
            @RequestParam UUID userId,
            @RequestParam UUID copyId
    ) {
        BookingDto booking = circulationService.issueDirectCopy(userId, copyId);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/return/{bookingId}")
    public ResponseEntity<BookingDto> returnBook(@PathVariable UUID bookingId) {
        BookingDto booking = circulationService.returnBook(bookingId);
        return ResponseEntity.ok(booking);
    }
}