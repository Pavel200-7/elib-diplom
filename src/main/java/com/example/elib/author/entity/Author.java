package com.example.elib.author.entity;

import com.example.elib.common.entity.base.BaseEntity;
import com.example.elib.country.entity.Country;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    public static Author create(String name, Country country) {
        Author author = new Author();
        author.name = name;
        author.country = country;
        author.validate();
        return author;
    }

    public void update(String name, Country country) {
        this.name = name;
        this.country = country;
        validate();
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя автора не может быть пустым.");
        }
        if (!name.matches("^[A-Za-zА-Яа-яЁё\\s\\-']+$")) {
            throw new IllegalArgumentException(
                    "Имя автора может содержать только буквы (русские или английские), пробелы, дефис и апостроф."
            );
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Имя автора не может превышать 255 символов.");
        }
        if (country == null) {
            throw new IllegalArgumentException("Страна автора обязательна.");
        }
    }
}