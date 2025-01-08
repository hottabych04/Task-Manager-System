package com.hottabych04.app.service.comment;

import com.hottabych04.app.controller.comment.payload.CommentCreateDto;
import com.hottabych04.app.controller.comment.payload.CommentGetDto;
import com.hottabych04.app.database.entity.Comment;
import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.database.repository.CommentRepository;
import com.hottabych04.app.exception.comment.CommentNotFoundException;
import com.hottabych04.app.service.comment.mapper.CommentMapper;
import com.hottabych04.app.service.security.AuthorizationUtil;
import com.hottabych04.app.service.task.TaskService;
import com.hottabych04.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    private final UserService userService;
    private final TaskService taskService;
    private final AuthorizationUtil authorizationUtil;

    public CommentGetDto create(CommentCreateDto commentCreateDto, Authentication authentication){
        Task task = taskService.getTaskEntity(commentCreateDto.task());
        User author = userService.getUserEntity(authentication.getName());

        if (authorizationUtil.isUser(authentication) && !task.getPerformers().contains(author)){
            log.error("User: " + author.getEmail() + " dont have access to the task: " + task.getId());
            throw new AccessDeniedException("User: " + author.getEmail() + " dont have access to the task: " + task.getId());
        }

        Comment comment = Comment.builder()
                .message(commentCreateDto.message())
                .author(author)
                .task(task)
                .createdAt(LocalDateTime.now())
                .build();

        Comment newComment = commentRepository.save(comment);

        return commentMapper.toCommentGetDto(newComment);
    }

    public CommentGetDto getComment(Long id, Authentication authentication){
        return commentMapper.toCommentGetDto(
                getCommentEntity(id,authentication)
        );
    }

    public Comment getCommentEntity(Long id, Authentication authentication){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment: " + id + " is not found");
                    return new CommentNotFoundException("Comment not found", id.toString());
                });

        User user = userService.getUserEntity(authentication.getName());
        Task task = comment.getTask();

        if (authorizationUtil.isAdmin(authentication) ||
                comment.getAuthor().equals(user) ||
                task.getAuthor().equals(user) ||
                task.getPerformers().contains(user)
        ){
            return comment;
        }

        log.error("User: " + user.getEmail() + " dont have access to the task: " + task.getId());
        throw new AccessDeniedException("User: " + user.getEmail() + " dont have access to the task: " + task.getId());
    }

    public void delete(Long id, Authentication authentication){
        Comment comment = getCommentEntity(id, authentication);
        commentRepository.delete(comment);
    }
}
