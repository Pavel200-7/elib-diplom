package com.example.elib.booking.repository.spec;

import com.example.elib.booking.dto.request.GetBookingCriteria;
import com.example.elib.booking.entity.Booking;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface BookingSpecificationBuilder {
    Specification<Booking> fromCriteria(GetBookingCriteria criteria);
    Specification<Booking> forUser(GetBookingCriteria criteria);
    Specification<Booking> activeForUser(UUID userId);
    Specification<Booking> hasActiveReservationForBook(UUID userId, UUID bookId);
}