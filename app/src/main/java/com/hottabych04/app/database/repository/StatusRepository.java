package com.hottabych04.app.database.repository;

import com.hottabych04.app.database.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {

    Optional<Status> findByName(String name);

}
