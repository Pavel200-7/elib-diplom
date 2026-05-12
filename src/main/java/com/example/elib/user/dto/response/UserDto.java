package com.example.elib.user.dto.response;

import com.example.elib.user.enums.UserStatus;
import lombok.*;

import java.time.LocalDate;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserDto {
    private String readerBookNumber;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate birthDate;
    private UserStatus status;
}
