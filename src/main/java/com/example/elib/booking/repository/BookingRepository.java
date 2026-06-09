package com.example.elib.booking.repository;

import com.example.elib.book.entity.Book;
import com.example.elib.booking.entity.Booking;
import com.example.elib.booking.enums.BookingStatus;
import com.example.elib.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID>, JpaSpecificationExecutor<Booking> {

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.user = :user AND b.copy.book = :book AND b.status IN :statuses")
    boolean existsByUserAndCopyBookAndStatusIn(
            @Param("user") User user,
            @Param("book") Book book,
            @Param("statuses") List<BookingStatus> statuses
    );
}
