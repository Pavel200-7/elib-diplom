package com.example.elib.author.repository;

import com.example.elib.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {
    boolean existsByName(String name);
    boolean existsByCountryId(UUID id);
    List<Author> findAllByOrderByNameAsc();
}