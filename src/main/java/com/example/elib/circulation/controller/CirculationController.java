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

    @PostMapping("/issue/direct/copy")
    public ResponseEntity<BookingDto> issueCopy(
            @RequestParam UUID userId,
            @RequestParam UUID copyId
    ) {
        BookingDto booking = circulationService.issueCopy(userId, copyId);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/return/{bookingId}")
    public ResponseEntity<BookingDto> returnBook(@PathVariable UUID bookingId) {
        BookingDto booking = circulationService.returnBook(bookingId);
        return ResponseEntity.ok(booking);
    }
}