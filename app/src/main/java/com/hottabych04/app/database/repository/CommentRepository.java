package com.hottabych04.app.database.repository;

import com.hottabych04.app.database.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
