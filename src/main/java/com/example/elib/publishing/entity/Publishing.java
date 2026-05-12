package com.example.elib.publishing.entity;

import com.example.elib.common.entity.BaseEntity;
import com.example.elib.country.entity.Country;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "publishings")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Publishing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    public static Publishing create(String name, String description, Country country) {
        Publishing publishing = new Publishing();
        publishing.name = name;
        publishing.description = description;
        publishing.country = country;
        publishing.validate();
        return publishing;
    }

    public void update(String name, String description, Country country) {
        this.name = name;
        this.description = description;
        this.country = country;
        validate();
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название издательства не может быть пустым.");
        }
        if (!name.matches("^[A-Za-zА-Яа-яЁё0-9\\s\\-'.]+$")) {
            throw new IllegalArgumentException(
                    "Название издательства может содержать только буквы (русские или английские), " +
                            "цифры, пробелы, дефис, апостроф и точку."
            );
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("Название издательства не может превышать 255 символов.");
        }
        if (country == null) {
            throw new IllegalArgumentException("Страна издательства обязательна.");
        }
    }
}