package com.example.elib.user.dto.response;

import com.example.elib.user.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserDto {
    String readerBookNumber;
    String email;
    String phone;
    String firstName;
    String lastName;
    String patronymic;
    LocalDate birthDate;
    UserStatus status;
}
