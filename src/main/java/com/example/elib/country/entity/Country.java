package com.example.elib.country.entity;

import com.example.elib.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public static Country create(String name) {
        Country country = new Country();
        country.name = name;
        country.validate();
        return country;
    }

    public void update(String name) {
        this.name = name;
        validate();
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название страны не может быть пустым");
        }
        if (!name.matches("^[A-Za-zА-Яа-яЁё\\s]+$")) {
            throw new IllegalArgumentException("Название страны может содержать только буквы (русские или английские) и пробелы");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Название страны не может превышать 100 символов");
        }
    }
}