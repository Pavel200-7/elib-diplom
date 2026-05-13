package com.example.elib.copy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Value
@Builder
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UpdateCopyDto {
    @NotBlank
    private String inventoryNumber;

    @NotBlank
    private String isbn;
}
