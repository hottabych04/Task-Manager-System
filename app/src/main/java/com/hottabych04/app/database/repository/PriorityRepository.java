package com.hottabych04.app.database.repository;

import com.hottabych04.app.database.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority, Integer> {

    Optional<Priority> findByName(String name);

}
