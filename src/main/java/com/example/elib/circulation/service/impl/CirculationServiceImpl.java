package com.example.elib.circulation.service.impl;

import com.example.elib.booking.dto.request.CreateBookingDto;
import com.example.elib.booking.dto.response.BookingDto;
import com.example.elib.booking.service.BookingService;
import com.example.elib.circulation.service.CirculationService;
import com.example.elib.common.exeption.BusinessRuleException;
import com.example.elib.copy.dto.response.CopyDto;
import com.example.elib.copy.enums.CopyStatus;
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
    public BookingDto reserveBook(UUID userId, UUID bookId) {
        log.info("Бронирование книги {} для пользователя {}", bookId, userId);
        CopyDto availableCopy = copyService.getRandomAvailableCopyByBookId(bookId);
        copyService.setReserved(availableCopy.getId());

        CreateBookingDto bookingDto = CreateBookingDto.builder()
                .userId(userId)
                .copyId(availableCopy.getId())
                .build();
        BookingDto booking = bookingService.makeReservation(bookingDto);

        log.info("Создана бронь {} для книги {} (экземпляр {}, пользователь {})",
                booking.getId(), bookId, availableCopy.getId(), userId);

        return booking;
    }

    @Override
    @Transactional
    public BookingDto reserveCopy(UUID userId, UUID copyId) {
        log.info("Бронирование экземпляра {} для пользователя {}", copyId, userId);
        CopyDto copy = copyService.getCopy(copyId);
        copyService.setReserved(copyId);

        CreateBookingDto bookingDto = CreateBookingDto.builder()
                .userId(userId)
                .copyId(copyId)
                .build();
        BookingDto booking = bookingService.makeReservation(bookingDto);
        log.info("Создана бронь {} на экземпляр {} для пользователя {}",
                booking.getId(), copyId, userId);
        return booking;
    }

    @Override
    @Transactional
    public BookingDto cancelReservation(UUID bookingId) {
        log.info("Отмена брони {}", bookingId);
        BookingDto booking = bookingService.cancelReservation(bookingId);
        copyService.cancelReserve(booking.getCopy().getId());
        log.info("Отменена бронь {}, экземпляр {} возвращён в AVAILABLE",
                bookingId, booking.getCopy().getId());
        return booking;
    }


    @Override
    @Transactional
    public BookingDto issueFromReservation(UUID bookingId) {
        log.info("Выдача по брони {}", bookingId);
        BookingDto booking = bookingService.getBooking(bookingId);
        copyService.setIssued(booking.getCopy().getId());

        BookingDto issuedBooking = bookingService.issue(bookingId);
        log.info("Выдана книга по брони {}, экземпляр {}", bookingId, booking.getCopy().getId());
        return issuedBooking;
    }

    @Override
    @Transactional
    public BookingDto issueDirect(UUID userId, UUID bookId) {
        log.info("Прямая выдача книги {} пользователю {}", bookId, userId);
        CopyDto availableCopy = copyService.getRandomAvailableCopyByBookId(bookId);
        copyService.setIssued(availableCopy.getId());

        CreateBookingDto bookingDto = CreateBookingDto.builder()
                .userId(userId)
                .copyId(availableCopy.getId())
                .build();
        BookingDto booking = bookingService.makeIssue(bookingDto);
        log.info("Прямая выдача: создана запись {} (экземпляр {}, пользователь {})",
                booking.getId(), availableCopy.getId(), userId);
        return booking;
    }

    @Override
    @Transactional
    public BookingDto issueDirectCopy(UUID userId, UUID copyId) {
        log.info("Прямая выдача экземпляра {} пользователю {}", copyId, userId);
        copyService.setIssued(copyId);

        CreateBookingDto bookingDto = CreateBookingDto.builder()
                .userId(userId)
                .copyId(copyId)
                .build();
        BookingDto booking = bookingService.makeIssue(bookingDto);

        log.info("Прямая выдача: создана запись {} на экземпляр {}, пользователь {}",
                booking.getId(), copyId, userId);
        return booking;
    }

    @Override
    @Transactional
    public BookingDto returnBook(UUID bookingId) {
        log.info("Возврат книги по брони {}", bookingId);
        BookingDto booking = bookingService.getBooking(bookingId);
        copyService.setInTransit(booking.getCopy().getId());

        BookingDto returnedBooking = bookingService.makeReturning(bookingId);
        log.info("Возвращена книга по брони {}, экземпляр {} ожидает расстановки",
                bookingId, booking.getCopy().getId());
        return returnedBooking;
    }
}