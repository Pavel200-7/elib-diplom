package com.example.elib.user.vo;

import com.example.elib.common.validation.BeanValidator;
import com.example.elib.common.validation.anotations.CommonLetters;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonalData {

    @CommonLetters
    @Column(name = "first_name")
    private String firstName;

    @CommonLetters
    @Column(name = "last_name")
    private String lastName;

    @CommonLetters
    @Column(name = "patronymic")
    private String patronymic;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    public static PersonalData of(String firstName, String lastName, String patronymic, LocalDate birthDate) {
        PersonalData data = new PersonalData(
                normalizeName(firstName),
                normalizeName(lastName),
                normalizeName(patronymic),
                birthDate
        );
        BeanValidator.validateOrThrow(data);
        return data;
    }

    private static String normalizeName(String name) {
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        String lower = trimmed.toLowerCase();
        return lower.substring(0, 1).toUpperCase() + lower.substring(1);
    }
}