package com.example.elib.common.service.impl;

import com.example.elib.common.enums.Sequences;
import com.example.elib.common.repository.helper.SequenceCounter;
import com.example.elib.common.service.Generator;
import com.example.elib.common.service.ReaderNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ReaderNumberGeneratorImpl implements ReaderNumberGenerator {

    private static final String FORMAT = "%07d";

    private final Generator<String> delegate;

    public ReaderNumberGeneratorImpl(SequenceCounter sequenceCounter) {
        this.delegate = new SequenceNumberGenerator(
                sequenceCounter,
                Sequences.READERS_COUNTER,
                FORMAT
        );
    }

    @Override
    @Transactional
    public String generate() {
        return delegate.generate();
    }
}
