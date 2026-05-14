package com.example.elib.booking.service.impl;

import com.example.elib.booking.dto.request.CreateBookingDto;
import com.example.elib.booking.dto.request.GetBookingCriteria;
import com.example.elib.booking.dto.response.BookingDto;
import com.example.elib.booking.entity.Booking;
import com.example.elib.booking.mapper.BookingMapper;
import com.example.elib.booking.repository.BookingRepository;
import com.example.elib.booking.repository.spec.BookingSpecificationBuilder;
import com.example.elib.booking.service.BookingService;
import com.example.elib.common.exeption.ResourceNotFoundException;
import com.example.elib.copy.entity.Copy;
import com.example.elib.copy.repository.CopyRepository;
import com.example.elib.user.entity.User;
import com.example.elib.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    public final UserRepository userRepository;
    public final CopyRepository copyRepository;
    public final BookingMapper bookingMapper;
    private final BookingSpecificationBuilder specBuilder;

    @Override
    @Transactional
    public BookingDto makeReservation(CreateBookingDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с id " + dto.getUserId() + " не найден."));
        Copy copy = copyRepository.findById(dto.getCopyId())
                .orElseThrow(() -> new ResourceNotFoundException("Экземпляр с id " + dto.getCopyId() + " не найден."));

        Booking booking = Booking.makeReservation(user, copy);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingDto cancelReservation(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Бронь с id " + id + " не найдена."));
        booking.cancel();
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingDto makeIssue(CreateBookingDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("Пользователь с id " + dto.getUserId() + " не найден."));
        Copy copy = copyRepository.findById(dto.getCopyId())
                .orElseThrow(() -> new ResourceNotFoundException("Экземпляр с id " + dto.getCopyId() + " не найден."));

        Booking booking = Booking.makeIssue(user, copy);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingDto issue(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Бронь с id " + id + " не найдена."));
        booking.issue();
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingDto makeReturning(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Бронь с id " + id + " не найдена."));
        booking.finish();
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional
    public BookingDto getBooking(UUID id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Бронь с id " + id + " не найдена."));
        return bookingMapper.toDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getUserBookings(GetBookingCriteria criteria) {
        if (!userRepository.existsById(criteria.getUserId())) {
            throw new ResourceNotFoundException("Пользователь с id " + criteria.getUserId() + " не найден.");
        }

        Specification<Booking> spec = specBuilder.forUser(criteria);
        List<Booking> bookings = bookingRepository.findAll(spec);
        return bookings.stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getActiveUserBookings(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден.");
        }

        Specification<Booking> spec = specBuilder.activeForUser(userId);
        List<Booking> bookings = bookingRepository.findAll(spec);
        return bookings.stream()
                .map(bookingMapper::toDto)
                .toList();
    }
}
