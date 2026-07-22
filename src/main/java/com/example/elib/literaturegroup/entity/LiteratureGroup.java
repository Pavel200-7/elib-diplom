package com.example.elib.literaturegroup.entity;

import com.example.elib.common.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "literature_groups")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LiteratureGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    private LiteratureGroup(String name) {
        this.name = name;
    }

    public static LiteratureGroup create(String name) {
        LiteratureGroup group = new LiteratureGroup(name);
        group.validate();
        return group;
    }

    public void update(String name) {
        this.name = name;
        validate();
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название группы литературы не может быть пустым.");
        }
        if (!name.matches("^[A-Za-zА-Яа-яЁё0-9\\s\\-]+$")) {
            throw new IllegalArgumentException(
                    "Название группы литературы может содержать только буквы (русские или английские), " +
                            "цифры, пробелы и дефис."
            );
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("Название группы литературы не может превышать 100 символов.");
        }
    }
}