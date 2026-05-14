package com.example.elib.booking.dto.response;

import com.example.elib.booking.enums.BookingStatus;
import com.example.elib.copy.dto.response.CopyDto;
import com.example.elib.copy.entity.Copy;
import com.example.elib.user.dto.response.UserDto;
import com.example.elib.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class BookingDto {
    UUID id;
    UserDto user;
    CopyDto copy;
    LocalDateTime created;
    LocalDateTime started;
    LocalDateTime finishing;
    LocalDateTime finished;
    BookingStatus status;
}
