package com.example.elib.copy.repository;

import com.example.elib.book.entity.Book;
import com.example.elib.copy.entity.Copy;
import com.example.elib.copy.enums.CopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CopyRepository extends JpaRepository<Copy, UUID>, JpaSpecificationExecutor<Copy> {
    boolean existsByHolderId(UUID id);
    boolean existsByIsbn(String isbn);
    boolean existsByInventoryNumber(String existsByInventoryNumber);
    List<Copy> findByBookIdAndStatus(UUID bookId, CopyStatus status);
    boolean existsByBookId(UUID id);
    long countByBookIdAndStatus(UUID bookId, CopyStatus status);
}
