package com.example.elib.holder.entity;

import com.example.elib.common.entity.BaseEntity;
import com.example.elib.holder.enums.HolderType;
import com.example.elib.room.entity.Room;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "holders")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Holder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private HolderType type;

    public static Holder create(String name, Room room, HolderType type) {
        Holder holder = new Holder();
        holder.name = name;
        holder.room = room;
        holder.type = type;
        holder.validate();
        return holder;
    }

    public void update(String name, Room room, HolderType type) {
        this.name = name;
        this.room = room;
        this.type = type;
        validate();
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Название места хранения не может быть пустым");
        }
        if (!name.matches("^[A-Za-zА-Яа-яЁё0-9\\s]+$")) {
            throw new IllegalArgumentException("Название может содержать только буквы, цифры и пробелы");
        }
        if (room == null) {
            throw new IllegalArgumentException("Место хранения должно быть привязано к помещению");
        }
        if (type == null) {
            throw new IllegalArgumentException("Тип места хранения обязателен");
        }
    }
}