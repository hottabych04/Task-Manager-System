package com.hottabych04.app.database.repository;

import com.hottabych04.app.database.entity.Comment;
import com.hottabych04.app.database.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByTask(Task task, Pageable pageable);

}
