package com.example.elib.common.service.impl;

import com.example.elib.common.enums.Sequences;
import com.example.elib.common.repository.helper.SequenceCounter;
import com.example.elib.common.service.Generator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SequenceNumberGenerator implements Generator<String> {

    private final SequenceCounter sequenceCounter;
    private final Sequences sequence;
    private final String format;

    @Override
    public String generate() {
        long value = sequenceCounter.getNext(sequence);
        return String.format(format, value);
    }
}
