package com.example.elib.booking.repository.spec;

import com.example.elib.booking.dto.request.GetBookingCriteria;
import com.example.elib.booking.entity.Booking;
import com.example.elib.booking.enums.BookingStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class BookingSpecificationBuilderImpl implements BookingSpecificationBuilder {

    @Override
    public Specification<Booking> fromCriteria(GetBookingCriteria criteria) {
        List<Specification<Booking>> specs = new ArrayList<>();

        if (criteria != null) {
            specs.add(byStatus(criteria.getStatus()));
            specs.add(byUserId(criteria.getUserId()));
            specs.add(byCopyId(criteria.getCopyId()));
            specs.add(createdBetween(criteria.getCreatedFrom(), criteria.getCreatedTo()));
            specs.add(hasOverdue(criteria.getOverdueOnly()));
        }

        return buildFromList(specs);
    }

    @Override
    public Specification<Booking> forUser(GetBookingCriteria criteria) {
        List<Specification<Booking>> specs = new ArrayList<>();

        specs.add(byUserId(criteria.getUserId()));
        specs.add(byStatus(criteria.getStatus()));
        specs.add(byCopyId(criteria.getCopyId()));

        return buildFromList(specs);
    }

    @Override
    public Specification<Booking> activeForUser(UUID userId) {
        List<Specification<Booking>> specs = new ArrayList<>();

        specs.add(byUserId(userId));
        specs.add(notFinished());
        specs.add(notCancelled());

        return buildFromList(specs);
    }

    private Specification<Booking> buildFromList(List<Specification<Booking>> specs) {
        return specs.stream()
                .reduce(Specification::and)
                .orElse(Specification.where((root, query, cb) -> cb.conjunction()));
    }

    private Specification<Booking> byStatus(BookingStatus status) {
        return (root, query, cb) -> {
            if (status == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("status"), status);
        };
    }

    private Specification<Booking> byUserId(UUID userId) {
        return (root, query, cb) -> {
            if (userId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("user").get("id"), userId);
        };
    }

    private Specification<Booking> byCopyId(UUID copyId) {
        return (root, query, cb) -> {
            if (copyId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("copy").get("id"), copyId);
        };
    }

    private Specification<Booking> createdBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return cb.conjunction();
            }
            if (from == null) {
                return cb.lessThanOrEqualTo(root.get("created"), to);
            }
            if (to == null) {
                return cb.greaterThanOrEqualTo(root.get("created"), from);
            }
            return cb.between(root.get("created"), from, to);
        };
    }

    private Specification<Booking> hasOverdue(Boolean overdueOnly) {
        return (root, query, cb) -> {
            if (overdueOnly == null || !overdueOnly) {
                return cb.conjunction();
            }
            return cb.and(
                    cb.equal(root.get("status"), BookingStatus.ISSUED),
                    cb.lessThan(root.get("finishing"), LocalDateTime.now())
            );
        };
    }

    private Specification<Booking> notFinished() {
        return (root, query, cb) ->
                cb.notEqual(root.get("status"), BookingStatus.CLOSED);
    }

    private Specification<Booking> notCancelled() {
        return (root, query, cb) ->
                cb.notEqual(root.get("status"), BookingStatus.CANCELLED);
    }
}