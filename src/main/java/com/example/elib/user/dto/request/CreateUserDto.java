package com.example.elib.user.dto.request;

import lombok.*;

import java.util.UUID;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateUserDto {
    private UUID id;
    private String email;
    private String phone;
}
