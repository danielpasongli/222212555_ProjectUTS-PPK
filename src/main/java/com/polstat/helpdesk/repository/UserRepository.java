package com.polstat.helpdesk.repository;

import com.polstat.helpdesk.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Menggunakan EntityGraph untuk menghindari N+1 queries dan memuat role bersama user
    @EntityGraph(attributePaths = "role")
    Optional<User> findByUsername(String username);

    // Query untuk mencari user berdasarkan username dengan role-nya
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(@Param("username") String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.role = NULL WHERE u.id = :userId")
    void removeRoleFromUser(@Param("userId") Long userId);

}
