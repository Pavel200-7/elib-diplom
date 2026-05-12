package com.example.elib.user.dto.request;

import lombok.*;

import java.time.LocalDate;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdateUserDto {
    private String firstName;
    private String lastName;
    private String patronymic;
    private LocalDate birthDate;
}
