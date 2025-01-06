package com.hottabych04.app.database.repository;

import com.hottabych04.app.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
}
