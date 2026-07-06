package com.example.elib.common.entity.utils;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "sequence_counters")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SequenceCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "counter_name", nullable = false, unique = true)
    private String counterName;

    @Column(name = "current_value", nullable = false)
    private Long currentValue = 0L;

    @Version
    @Column(name = "version")
    private Long version;

    public SequenceCounter(String counterName) {
        this.counterName = counterName;
    }

    public Long nextValue() {
        currentValue++;
        return currentValue;
    }
}
