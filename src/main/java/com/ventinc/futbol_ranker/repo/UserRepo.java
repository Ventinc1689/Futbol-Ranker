package com.ventinc.futbol_ranker.repo;

import com.ventinc.futbol_ranker.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<Users> findByRole(String role);

    // Login flexibility: find a user by username or email
    @Query("SELECT u FROM Users u WHERE u.username = ?1 OR u.email = ?1")
    Optional<Users> findByUsernameOrEmail(String usernameOrEmail);
}
