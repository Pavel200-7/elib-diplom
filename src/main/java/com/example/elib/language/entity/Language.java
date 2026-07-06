package com.example.elib.language.entity;

import com.example.elib.common.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "languages")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Language extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public static Language create(String name) {
        Language language = new Language();
        language.name = name;
        language.validate();
        return language;
    }

    public void update(String name) {
        this.name = name;
        validate();
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название языка не может быть пустым.");
        }
        if (!name.matches("^[A-Za-zА-Яа-яЁё\\s\\-]+$")) {
            throw new IllegalArgumentException(
                    "Название языка может содержать только буквы (русские или английские), пробелы и дефис."
            );
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Название языка не может превышать 100 символов.");
        }
    }
}