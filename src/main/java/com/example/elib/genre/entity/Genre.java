package com.example.elib.genre.entity;

import com.example.elib.common.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Genre extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public static Genre create(String name) {
        Genre genre = new Genre();
        genre.name = name;
        genre.validate();
        return genre;
    }

    public void update(String name) {
        this.name = name;
        validate();
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название жанра не может быть пустым.");
        }
        if (!name.matches("^[A-Za-zА-Яа-яЁё0-9\\s\\-']+$")) {
            throw new IllegalArgumentException(
                    "Название жанра может содержать только буквы (русские или английские), " +
                            "цифры, пробелы, дефис и апостроф."
            );
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Название жанра не может превышать 100 символов.");
        }
    }
}