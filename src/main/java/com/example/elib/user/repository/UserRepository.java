package com.example.elib.user.repository;

import com.example.elib.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByContactEmail(String email);
    boolean existsByContactPhone(String phone);

    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.contact.email) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR u.contact.phone LIKE CONCAT('%', :query, '%')")
    List<User> searchByEmailOrPhone(@Param("query") String query);
}
