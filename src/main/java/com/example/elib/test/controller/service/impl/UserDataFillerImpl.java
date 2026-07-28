package com.example.elib.test.controller.service.impl;

import com.example.elib.test.controller.service.UserDataFiller;
import com.example.elib.test.controller.service.impl.helper.UserGenerator;
import com.example.elib.user.entity.User;
import com.example.elib.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDataFillerImpl implements UserDataFiller {

    private final UserRepository userRepository;
    private final UserGenerator userGenerator;

    private static final int BATCH_SIZE = 1000;
    private static final int TRANSACTION_BATCH = 10000;

    @Override
    public boolean fillUser(int rowsCount) {
        if (rowsCount <= 0) {
            log.warn("Неверное количество строк: {}", rowsCount);
            return false;
        }

        log.info("Начало заполнения таблицы users {} строками", rowsCount);
        long startTime = System.currentTimeMillis();

        AtomicInteger totalSaved = new AtomicInteger(0);

        int batches = (rowsCount + TRANSACTION_BATCH - 1) / TRANSACTION_BATCH;
        for (int batchIndex = 0; batchIndex < batches; batchIndex++) {
            int startRow = batchIndex * TRANSACTION_BATCH + 1;
            int endRow = Math.min((batchIndex + 1) * TRANSACTION_BATCH, rowsCount);
            int countInBatch = endRow - startRow + 1;

            log.info("Транзакция {}/{}: записи {} - {}",
                    batchIndex + 1, batches, startRow, endRow);

            int saved = fillBatch(countInBatch);
            totalSaved.addAndGet(saved);

            userRepository.flush();

            log.info("Транзакция {}/{} завершена. Сохранено: {}",
                    batchIndex + 1, batches, saved);
        }

        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;

        log.info("Заполнение завершено. Создано {} записей за {} секунд", totalSaved.get(), duration);
        log.info("Скорость: {} записей/сек", totalSaved.get() / (duration > 0 ? duration : 1));

        return totalSaved.get() > 0;
    }

    @Transactional
    protected int fillBatch(int rowsCount) {
        int saved = 0;
        List<User> batch = new ArrayList<>(BATCH_SIZE);

        for (int i = 0; i < rowsCount; i++) {
            try {
                User user = generateUniqueUser();
                batch.add(user);
                saved++;

                if (batch.size() >= BATCH_SIZE) {
                    userRepository.saveAll(batch);
                    batch.clear();
                }
            } catch (Exception e) {
                log.error("Ошибка при генерации пользователя {}", i, e);
            }
        }

        if (!batch.isEmpty()) {
            userRepository.saveAll(batch);
        }

        return saved;
    }

    private User generateUniqueUser() {
        User user = userGenerator.Generate();

        int attempts = 0;
        while (attempts < 5) {
            if (!userRepository.existsByContactEmail(user.getEmail()) &&
                    !userRepository.existsByContactPhone(user.getPhone())) {
                break;
            }
            user = userGenerator.Generate();
            attempts++;
        }

        if (attempts >= 5) {
            log.warn("Не удалось сгенерировать уникальные email/phone после 5 попыток");
        }

        return user;
    }
}