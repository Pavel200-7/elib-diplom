package com.example.elib.circulation.service.impl;

import com.example.elib.booking.dto.request.CreateBookingDto;
import com.example.elib.booking.dto.response.BookingDto;
import com.example.elib.booking.service.BookingService;
import com.example.elib.circulation.service.CirculationService;
import com.example.elib.copy.dto.response.CopyDto;
import com.example.elib.copy.service.CopyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CirculationServiceImpl implements CirculationService {

    private final CopyService copyService;
    private final BookingService bookingService;

    @Override
    @Transactional
    public BookingDto issueCopy(UUID userId, UUID copyId) {
        log.info("Выдача экземпляра {} пользователю {}", copyId, userId);
        copyService.setIssued(copyId);

        CreateBookingDto bookingDto = CreateBookingDto.builder()
                .userId(userId)
                .copyId(copyId)
                .build();
        BookingDto booking = bookingService.makeIssue(bookingDto);

        log.info("Выдача: создана запись {} на экземпляр {}, пользователь {}",
                booking.getId(), copyId, userId);
        return booking;
    }

    @Override
    @Transactional
    public BookingDto returnBook(UUID bookingId) {
        log.info("Возврат книги {}", bookingId);
        BookingDto booking = bookingService.getBooking(bookingId);
        copyService.setInTransit(booking.getCopy().getId());

        BookingDto returnedBooking = bookingService.makeReturning(bookingId);
        log.info("Возвращена книга {}, экземпляр {} ожидает расстановки",
                bookingId, booking.getCopy().getId());
        return returnedBooking;
    }
}