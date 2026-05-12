package com.example.elib.holder.repository;

import com.example.elib.holder.entity.Holder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HolderRepository extends JpaRepository<Holder, UUID> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, UUID id);
    boolean existsByRoomId(UUID id);
}