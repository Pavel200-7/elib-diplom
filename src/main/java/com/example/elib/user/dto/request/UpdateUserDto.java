package com.example.elib.user.dto.request;

import lombok.*;

import java.time.LocalDate;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdateUserDto {
    String firstName;
    String lastName;
    String patronymic;
    LocalDate birthDate;
}
