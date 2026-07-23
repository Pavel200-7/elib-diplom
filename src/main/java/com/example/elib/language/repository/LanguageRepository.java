package com.example.elib.language.repository;

import com.example.elib.language.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LanguageRepository extends JpaRepository<Language, UUID> {
    boolean existsByName(String name);
    List<Language> findAllByOrderByNameAsc();
}