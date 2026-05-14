package com.example.elib.book.repository;

import com.example.elib.book.entity.Book;
import com.example.elib.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {
    boolean existsByName(String name);
    boolean existsByPublishingId(UUID id);
    boolean existsByLiteratureGroupId(UUID id);
    boolean existsByLanguageId(UUID id);
    boolean existsByGenreId(UUID id);
    boolean existsByAuthorId(UUID id);
}