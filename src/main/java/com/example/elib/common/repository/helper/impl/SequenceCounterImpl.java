package com.example.elib.common.repository.helper.impl;


import com.example.elib.common.entity.utils.Sequence;
import com.example.elib.common.enums.Sequences;
import com.example.elib.common.repository.SequenceRepository;
import com.example.elib.common.repository.helper.SequenceCounter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SequenceCounterImpl implements SequenceCounter {

    private final SequenceRepository repository;
    private final EntityManager entityManager;

    private static final int MAX_RETRIES = 10;

    @Override
    public long getNext(Sequences targetSequence) {
        String sequenceName = targetSequence.getName();

        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                entityManager.clear();

                Sequence sequence = getSequence(sequenceName);
                Long value = sequence.nextValue();
                repository.save(sequence);

                entityManager.flush();

                return value;
            } catch (OptimisticLockException e) {
                retries++;
                log.warn("Конфликт при извлечении следующего элемента последовательности, попытка {}", retries);
                entityManager.clear();
            }
        }
        throw new RuntimeException("Не удалось извлечь следующий элемент последовательности после " + MAX_RETRIES + " попыток");
    }

    private Sequence getSequence(String sequenceName) {
        return repository
            .findBySequenceName(sequenceName)
            .orElseGet(() -> {
                Sequence newSequence = new Sequence(sequenceName);
                return repository.save(newSequence);
            });
    }
}
