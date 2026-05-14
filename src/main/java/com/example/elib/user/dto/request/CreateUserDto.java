package com.example.elib.user.dto.request;

import lombok.*;

import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateUserDto {
    UUID id;
    String email;
    String phone;
}
