package com.hottabych04.app.database.repository;

import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAuthor(User author);

    List<Task> findByPerformersContains(User performer);

}
