package com.hottabych04.app.database.repository;

import com.hottabych04.app.database.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
