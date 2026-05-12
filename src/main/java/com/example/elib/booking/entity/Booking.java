package com.example.elib.booking.entity;

import com.example.elib.booking.enums.BookingStatus;
import com.example.elib.common.entity.BaseEntity;
import com.example.elib.copy.Copy;
import com.example.elib.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "copy_id")
    private Copy copy;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Column(name = "started")
    private LocalDateTime started;

    @Column(name = "finishing")
    private LocalDateTime finishing;

    @Column(name = "finished")
    private LocalDateTime finished;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private BookingStatus status;

}