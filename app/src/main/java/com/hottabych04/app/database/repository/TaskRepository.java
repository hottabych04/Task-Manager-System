package com.hottabych04.app.database.repository;

import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.database.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByAuthor(User author, Pageable pageable);

    Page<Task> findByPerformersContains(User performer, Pageable pageable);

}
