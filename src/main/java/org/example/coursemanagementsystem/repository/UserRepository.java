package org.example.coursemanagementsystem.repository;

import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findByRole(UserRole role);

    List<User> findByIsActive(Boolean isActive);

    // Tìm user theo username và isActive = true (dùng cho login)
    Optional<User> findByUsernameAndIsActiveTrue(String username);
}