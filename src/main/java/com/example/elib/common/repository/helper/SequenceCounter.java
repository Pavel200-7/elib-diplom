package com.example.elib.common.repository.helper;

import com.example.elib.common.enums.Sequences;

public interface SequenceCounter {
    long getNext(Sequences sequence);
}
