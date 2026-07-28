package com.example.elib.common.entity.utils;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "sequences")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "current_value", nullable = false)
    private Long currentValue = 0L;

    @Version
    @Column(name = "version")
    private Long version;

    public Sequence(String name) {
        this.name = name;
    }

    public Long nextValue() {
        currentValue++;
        return currentValue;
    }
}
