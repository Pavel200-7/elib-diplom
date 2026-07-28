package com.example.elib.user.repository;

import com.example.elib.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query(value = """
        SELECT * FROM get_users_with_full_text_search(:query)
        LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<User> searchByQuery(
            @Param("query") String query,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Query(value = """
        SELECT COUNT(*) FROM get_users_with_full_text_search(:query)
    """, nativeQuery = true)
    long countByQuery(@Param("query") String query);
}
