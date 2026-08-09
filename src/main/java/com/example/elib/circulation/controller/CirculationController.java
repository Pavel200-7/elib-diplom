package com.example.elib.circulation.controller;

import com.example.elib.booking.dto.response.BookingDto;
import com.example.elib.circulation.service.CirculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/circulation")
@RequiredArgsConstructor
public class CirculationController {

    private final CirculationService circulationService;

    @PostMapping("/issue")
    public ResponseEntity<BookingDto> issueCopy(
            @RequestParam UUID userId,
            @RequestParam UUID copyId
    ) {
        BookingDto booking = circulationService.issueCopy(userId, copyId);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/return/{bookingId}")
    public ResponseEntity<BookingDto> returnCopy(@PathVariable UUID bookingId) {
        BookingDto booking = circulationService.returnCopy(bookingId);
        return ResponseEntity.ok(booking);
    }
}