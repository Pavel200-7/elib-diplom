package com.example.elib.common.repository;

import com.example.elib.common.entity.utils.SequenceCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SequenceCounterRepository extends JpaRepository<SequenceCounter, Long> {

    @Query("SELECT sc FROM SequenceCounter sc WHERE sc.counterName = :name")
    Optional<SequenceCounter> findByCounterName(String name);
}