package com.example.elib.holder.repository;

import com.example.elib.holder.entity.Holder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HolderRepository extends JpaRepository<Holder, UUID> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, UUID id);
    boolean existsByRoomId(UUID id);

    @Query(value = """
            SELECT DISTINCT h
            FROM Holder h
            JOIN FETCH h.room
            ORDER BY h.name
            """)
    List<Holder> getAllWithRoom();

}