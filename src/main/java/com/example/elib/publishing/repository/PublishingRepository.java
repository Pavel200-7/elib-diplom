package com.example.elib.publishing.repository;

import com.example.elib.publishing.entity.Publishing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PublishingRepository extends JpaRepository<Publishing, UUID> {
    boolean existsByName(String name);
    boolean existsByCountryId(UUID id);
}