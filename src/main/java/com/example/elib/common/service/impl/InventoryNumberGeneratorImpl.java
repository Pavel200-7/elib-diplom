package com.example.elib.common.service.impl;

import com.example.elib.common.entity.utils.SequenceCounter;
import com.example.elib.common.enums.Counters;
import com.example.elib.common.repository.SequenceCounterRepository;
import com.example.elib.common.service.InventoryNumberGenerator;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryNumberGeneratorImpl implements InventoryNumberGenerator {

    private final SequenceCounterRepository repository;

    private static final int MAX_RETRIES = 10;

    private static final String PREFIX = "INV-";
    private static final int DIGITS = 8;
    private static final String FORMAT = "%08d";

    @Override
    @Transactional
    public String generate() {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                SequenceCounter counter = getCounter(Counters.COPIES_COUNTER.getName());
                Long value = counter.nextValue();
                repository.save(counter);
                return formatInventoryNumber(value);
            } catch (OptimisticLockException e) {
                retries++;
                log.warn("Конфликт при генерации номера, попытка {}", retries);
            }
        }
        throw new RuntimeException("Не удалось сгенерировать номер после " + MAX_RETRIES + " попыток");
    }

    private SequenceCounter getCounter(String counterName) {
        return repository
                .findByCounterName(counterName)
                .orElseGet(() -> {
                    SequenceCounter newCounter = new SequenceCounter(counterName);
                    return repository.save(newCounter);
                });
    }

    private String formatInventoryNumber(Long number) {
        String formattedNumber = String.format(FORMAT, number);
        String inventoryNumber = PREFIX + formattedNumber;
        return inventoryNumber;
    }
}
