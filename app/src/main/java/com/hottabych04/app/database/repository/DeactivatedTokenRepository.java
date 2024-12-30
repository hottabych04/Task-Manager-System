package com.hottabych04.app.database.repository;

import com.hottabych04.app.database.entity.DeactivatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeactivatedTokenRepository extends JpaRepository<DeactivatedToken, UUID> {
}
