package com.infnet.orbis_login.repository;

import com.infnet.orbis_login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> getUserById(Long id);
}
