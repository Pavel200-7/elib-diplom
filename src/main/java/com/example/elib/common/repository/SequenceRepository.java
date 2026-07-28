package com.example.elib.common.repository;

import com.example.elib.common.entity.utils.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SequenceRepository extends JpaRepository<Sequence, UUID> {
    @Query("SELECT s FROM Sequence s WHERE s.name = :name")
    Optional<Sequence> findBySequenceName(String name);
}