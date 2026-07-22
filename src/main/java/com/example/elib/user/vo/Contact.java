package com.example.elib.user.vo;

import com.example.elib.common.validation.BeanValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Contact {

    @Email(message = "Неверный формат email")
    @Size(max = 254, message = "Превышено ограничение в 254 символа")
    @Column(name = "email", nullable = false)
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{10,20}$", message = "Неверный формат телефона")
    @Column(name = "phone", nullable = false)
    private String phone;

    public static Contact of(String email, String phone) {
        Contact contact = new Contact(email, phone);
        BeanValidator.validateOrThrow(contact);
        return contact;
    }
}
