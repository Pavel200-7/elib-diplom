package com.example.elib.copy.repository;

import com.example.elib.copy.entity.Copy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CopyRepository extends JpaRepository<Copy, UUID> {
    boolean existsByHolderId(UUID id);
    boolean existsByIsbn(String isbn);
    boolean existsByInventoryNumber(String existsByInventoryNumber);


}
