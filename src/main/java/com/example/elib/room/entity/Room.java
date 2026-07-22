package com.example.elib.room.entity;

import com.example.elib.common.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    private Room(String name) {
        this.name = name;
    }

    public static Room create(String name) {
        return new Room(name);
    }

    public void update(String name) {
        this.name = name;
    }

}