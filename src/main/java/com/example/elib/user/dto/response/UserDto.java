package com.example.elib.user.dto.response;

import com.example.elib.user.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserDto {
    UUID id;
    String readerBookNumber;
    String email;
    String phone;
    String firstName;
    String lastName;
    String patronymic;
    LocalDate birthDate;
    UserStatus status;
}
