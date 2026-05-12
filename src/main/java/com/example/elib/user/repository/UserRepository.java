package com.example.elib.user.repository;

import com.example.elib.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByContactEmail(String email);
    boolean existsByContactPhone(String phone);

}
