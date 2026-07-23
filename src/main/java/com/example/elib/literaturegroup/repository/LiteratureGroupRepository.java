package com.example.elib.literaturegroup.repository;

import com.example.elib.literaturegroup.entity.LiteratureGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LiteratureGroupRepository extends JpaRepository<LiteratureGroup, UUID> {
    boolean existsByName(String name);
    List<LiteratureGroup> findAllByOrderByNameAsc();
}