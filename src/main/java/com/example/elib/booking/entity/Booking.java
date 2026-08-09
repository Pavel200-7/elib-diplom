package com.example.elib.booking.entity;

import com.example.elib.booking.enums.BookingStatus;
import com.example.elib.common.entity.base.BaseEntity;
import com.example.elib.copy.entity.Copy;
import com.example.elib.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "copy_id", nullable = false)
    private Copy copy;

    @Column(name = "started", nullable = false)
    private LocalDateTime started;

    @Column(name = "finishing")
    private LocalDateTime finishing;

    @Column(name = "finished")
    private LocalDateTime finished;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;

    private Booking(User user, Copy copy) {
        this.user = user;
        this.copy = copy;
    }

    public static Booking makeIssue(User user, Copy copy) {
        Booking booking = new Booking(user, copy);
        setIssued(booking);
        return booking;
    }

    public void issue() {
        setIssued(this);
    }

    private static void setIssued(Booking booking) {
        booking.status = BookingStatus.ISSUED;
        booking.started = LocalDateTime.now();
        booking.finishing = LocalDateTime.now()
                .plusMonths(2);
    }

    public void finish() {
        if (!isInStatus(BookingStatus.ISSUED)) {
            throw new IllegalStateException("Можно вернуть только выданную книгу.");
        }
        this.status = BookingStatus.CLOSED;
        this.finished = LocalDateTime.now();
    }

    public boolean isInStatus(BookingStatus status) {
        return this.status == status;
    }

}