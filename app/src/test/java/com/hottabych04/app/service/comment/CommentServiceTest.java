package com.hottabych04.app.service.comment;

import com.hottabych04.app.controller.comment.payload.CommentCreateDto;
import com.hottabych04.app.controller.comment.payload.CommentGetDto;
import com.hottabych04.app.controller.user.payload.UserIdsDto;
import com.hottabych04.app.database.entity.Comment;
import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.database.repository.CommentRepository;
import com.hottabych04.app.exception.comment.CommentNotFoundException;
import com.hottabych04.app.exception.comment.CommentPermissionException;
import com.hottabych04.app.service.comment.mapper.CommentMapper;
import com.hottabych04.app.service.security.AuthorizationUtil;
import com.hottabych04.app.service.task.TaskService;
import com.hottabych04.app.service.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceTest {

    private static final String DUMMY_EMAIL = "dummy@example.com";
    private static final String DUMMY = "dummy";

    @Mock
    private CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private CommentMapper commentMapper = Mockito.mock(CommentMapper.class);
    private UserService userService = Mockito.mock(UserService.class);
    private TaskService taskService = Mockito.mock(TaskService.class);
    private AuthorizationUtil authorizationUtil = Mockito.mock(AuthorizationUtil.class);

    private CommentService commentService = new CommentService(
            commentRepository,
            commentMapper,
            userService,
            taskService,
            authorizationUtil
    );

    @Test
    @DisplayName("Success comment create for admin/author")
    public void createCommentForAdmin(){
        CommentCreateDto commentCreateDto = new CommentCreateDto(
                DUMMY,
                1L
        );
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(commentCreateDto.task())
                .performers(List.of())
                .build();
        Comment dummyComment = Comment.builder()
                .message(commentCreateDto.message())
                .author(dummyUser)
                .task(dummyTask)
                .createdAt(LocalDateTime.MIN)
                .build();
        CommentGetDto commentGetDto = new CommentGetDto(
                null,
                dummyComment.getMessage(),
                new UserIdsDto(dummyUser.getId(), dummyUser.getEmail()),
                dummyComment.getTask().getId(),
                dummyComment.getCreatedAt()
        );

        Mockito.doReturn(dummyTask).when(taskService).getTaskEntity(commentCreateDto.task());
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(true);
        Mockito.when(commentRepository.save(dummyComment)).thenReturn(dummyComment);
        Mockito.when(commentMapper.toCommentGetDto(dummyComment)).thenReturn(commentGetDto);

        try (MockedStatic<LocalDateTime> mockStatic = Mockito.mockStatic(LocalDateTime.class)) {
            mockStatic.when(LocalDateTime::now).thenReturn(LocalDateTime.MIN);

            CommentGetDto comment = commentService.create(commentCreateDto, authentication);
            assertThat(comment).isEqualTo(commentGetDto);
        }
    }

    @Test
    @DisplayName("Success comment create for performer")
    public void createCommentForPerformer(){
        CommentCreateDto commentCreateDto = new CommentCreateDto(
                DUMMY,
                1L
        );
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(commentCreateDto.task())
                .performers(List.of(dummyUser))
                .build();
        Comment dummyComment = Comment.builder()
                .message(commentCreateDto.message())
                .author(dummyUser)
                .task(dummyTask)
                .createdAt(LocalDateTime.MIN)
                .build();
        CommentGetDto commentGetDto = new CommentGetDto(
                null,
                dummyComment.getMessage(),
                new UserIdsDto(dummyUser.getId(), dummyUser.getEmail()),
                dummyComment.getTask().getId(),
                dummyComment.getCreatedAt()
        );

        Mockito.doReturn(dummyTask).when(taskService).getTaskEntity(commentCreateDto.task());
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(false);
        Mockito.when(commentRepository.save(dummyComment)).thenReturn(dummyComment);
        Mockito.when(commentMapper.toCommentGetDto(dummyComment)).thenReturn(commentGetDto);

        try (MockedStatic<LocalDateTime> mockStatic = Mockito.mockStatic(LocalDateTime.class)) {
            mockStatic.when(LocalDateTime::now).thenReturn(LocalDateTime.MIN);

            CommentGetDto comment = commentService.create(commentCreateDto, authentication);
            assertThat(comment).isEqualTo(commentGetDto);
        }
    }

    @Test
    @DisplayName("Try to create a comment by a user who does not have sufficient rights")
    public void throwCommentPermissionExceptionWhenCreateComment(){
        CommentCreateDto commentCreateDto = new CommentCreateDto(
                DUMMY,
                1L
        );
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(commentCreateDto.task())
                .performers(List.of())
                .build();

        Mockito.doReturn(dummyTask).when(taskService).getTaskEntity(commentCreateDto.task());
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(false);

        assertThrows(CommentPermissionException.class, () -> commentService.create(commentCreateDto, authentication));
    }

    @Test
    @DisplayName("Success get CommentGetDto by id for admin/author")
    public void getCommentForAdmin(){
        Long id = 1L;
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(id)
                .performers(List.of())
                .build();
        Comment dummyComment = Comment.builder()
                .message(DUMMY)
                .author(dummyUser)
                .task(dummyTask)
                .createdAt(LocalDateTime.MIN)
                .build();
        CommentGetDto commentGetDto = new CommentGetDto(
                id,
                dummyComment.getMessage(),
                new UserIdsDto(dummyUser.getId(), dummyUser.getEmail()),
                dummyComment.getTask().getId(),
                dummyComment.getCreatedAt()
        );

        Mockito.when(commentRepository.findById(id)).thenReturn(Optional.of(dummyComment));
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(true);
        Mockito.when(commentMapper.toCommentGetDto(dummyComment)).thenReturn(commentGetDto);

        CommentGetDto comment = commentService.getComment(id, authentication);
        assertThat(comment).isEqualTo(commentGetDto);
    }

    @Test
    @DisplayName("Success get CommentGetDto by id for performer")
    public void getCommentForPerformer(){
        Long id = 1L;
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(id)
                .performers(List.of(dummyUser))
                .build();
        Comment dummyComment = Comment.builder()
                .message(DUMMY)
                .author(dummyUser)
                .task(dummyTask)
                .createdAt(LocalDateTime.MIN)
                .build();
        CommentGetDto commentGetDto = new CommentGetDto(
                id,
                dummyComment.getMessage(),
                new UserIdsDto(dummyUser.getId(), dummyUser.getEmail()),
                dummyComment.getTask().getId(),
                dummyComment.getCreatedAt()
        );

        Mockito.when(commentRepository.findById(id)).thenReturn(Optional.of(dummyComment));
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(false);
        Mockito.when(commentMapper.toCommentGetDto(dummyComment)).thenReturn(commentGetDto);

        CommentGetDto comment = commentService.getComment(id, authentication);
        assertThat(comment).isEqualTo(commentGetDto);
    }

    @Test
    @DisplayName("Success get Comment entity by id for admin/author")
    public void getCommentEntityForAdmin(){
        Long id = 1L;
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(id)
                .performers(List.of())
                .build();
        Comment dummyComment = Comment.builder()
                .message(DUMMY)
                .author(dummyUser)
                .task(dummyTask)
                .createdAt(LocalDateTime.MIN)
                .build();

        Mockito.when(commentRepository.findById(id)).thenReturn(Optional.of(dummyComment));
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(true);

        Comment comment = commentService.getCommentEntity(id, authentication);
        assertThat(comment).isEqualTo(dummyComment);
    }

    @Test
    @DisplayName("Success get Comment entity by id for performer")
    public void getCommentEntityForPerformer(){
        Long id = 1L;
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(id)
                .performers(List.of(dummyUser))
                .build();
        Comment dummyComment = Comment.builder()
                .message(DUMMY)
                .author(dummyUser)
                .task(dummyTask)
                .createdAt(LocalDateTime.MIN)
                .build();

        Mockito.when(commentRepository.findById(id)).thenReturn(Optional.of(dummyComment));
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(false);

        Comment comment = commentService.getCommentEntity(id, authentication);
        assertThat(comment).isEqualTo(dummyComment);
    }

    @Test
    @DisplayName("Try get non-existing Comment entity by id")
    public void throwCommentNotFoundExceptionWhenGetCommentEntity(){
        Long id = 1L;
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );

        Mockito.when(commentRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.getCommentEntity(id, authentication));
    }

    @Test
    @DisplayName("Try get Comment entity by id by a user who does not have sufficient rights")
    public void throwCommentPermissionExceptionWhenGetCommentEntity(){
        Long id = 1L;
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(id)
                .performers(List.of())
                .build();
        Comment dummyComment = Comment.builder()
                .message(DUMMY)
                .author(dummyUser)
                .task(dummyTask)
                .createdAt(LocalDateTime.MIN)
                .build();

        Mockito.when(commentRepository.findById(id)).thenReturn(Optional.of(dummyComment));
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(false);

        assertThrows(CommentPermissionException.class, () -> commentService.getCommentEntity(id, authentication));
    }

    @Test
    @DisplayName("Success get comments by admin")
    public void getCommentsByAdmin(){
        Long taskId = 1L;
        Integer page = 1, size = 1;
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(taskId)
                .performers(List.of())
                .build();
        Comment dummyComment = Comment.builder()
                .id(1L)
                .message(DUMMY)
                .author(dummyUser)
                .task(dummyTask)
                .createdAt(LocalDateTime.MIN)
                .build();
        CommentGetDto commentGetDto = new CommentGetDto(
                null,
                dummyComment.getMessage(),
                new UserIdsDto(dummyUser.getId(), dummyUser.getEmail()),
                dummyComment.getTask().getId(),
                dummyComment.getCreatedAt()
        );
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt"));
        Page<Comment> commentPage = new PageImpl<>(
                List.of(dummyComment),
                pageable,
                2
        );
        Page<CommentGetDto> commentgetDtoPage = commentPage.map(it -> commentGetDto);

        Mockito.when(taskService.getTaskEntity(taskId)).thenReturn(dummyTask);
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(true);
        Mockito.when(commentRepository.findByTask(dummyTask, pageable)).thenReturn(commentPage);
        Mockito.when(commentMapper.toCommentGetDto(dummyComment)).thenReturn(commentGetDto);

        Page<CommentGetDto> comments = commentService.getComments(taskId, page, size, authentication);

        assertThat(comments).isEqualTo(commentgetDtoPage);
    }

    @Test
    @DisplayName("Success get comments by performer")
    public void getCommentsByPerformer(){
        Long taskId = 1L;
        Integer page = 1, size = 1;
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(taskId)
                .performers(List.of(dummyUser))
                .build();
        Comment dummyComment = Comment.builder()
                .id(1L)
                .message(DUMMY)
                .author(dummyUser)
                .task(dummyTask)
                .createdAt(LocalDateTime.MIN)
                .build();
        CommentGetDto commentGetDto = new CommentGetDto(
                null,
                dummyComment.getMessage(),
                new UserIdsDto(dummyUser.getId(), dummyUser.getEmail()),
                dummyComment.getTask().getId(),
                dummyComment.getCreatedAt()
        );
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt"));
        Page<Comment> commentPage = new PageImpl<>(
                List.of(dummyComment),
                pageable,
                2
        );
        Page<CommentGetDto> commentgetDtoPage = commentPage.map(it -> commentGetDto);

        Mockito.when(taskService.getTaskEntity(taskId)).thenReturn(dummyTask);
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(false);
        Mockito.when(commentRepository.findByTask(dummyTask, pageable)).thenReturn(commentPage);
        Mockito.when(commentMapper.toCommentGetDto(dummyComment)).thenReturn(commentGetDto);

        Page<CommentGetDto> comments = commentService.getComments(taskId, page, size, authentication);

        assertThat(comments).isEqualTo(commentgetDtoPage);
    }

    @Test
    @DisplayName("Try to get a comments by a user who does not have sufficient rights")
    public void throwCommentPermissionExceptionWhenGetCommentsByPerformer(){
        Long taskId = 1L;
        Integer page = 1, size = 1;
        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Task dummyTask = Task.builder()
                .id(taskId)
                .performers(List.of())
                .build();

        Mockito.when(taskService.getTaskEntity(taskId)).thenReturn(dummyTask);
        Mockito.when(userService.getUserEntity(authentication.getName())).thenReturn(dummyUser);
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(false);

        assertThrows(CommentPermissionException.class, () -> commentService.getComments(taskId, page, size, authentication));
    }

    @Test
    @DisplayName("Success delete comment by id for author")
    public void deleteCommentForAuthor(){
        Long id = 1L;

        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email(DUMMY_EMAIL)
                .build();
        Comment dummyComment = Comment.builder()
                .id(id)
                .message(DUMMY)
                .author(dummyUser)
                .createdAt(LocalDateTime.MIN)
                .build();

        Mockito.when(commentRepository.findById(id)).thenReturn(Optional.of(dummyComment));
        Mockito.doNothing().when(commentRepository).delete(dummyComment);

        commentService.delete(id, authentication);
    }

    @Test
    @DisplayName("Success delete comment by id for admin")
    public void deleteCommentForAdmin(){
        Long id = 1L;

        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email("dummy2@example.com")
                .build();
        Comment dummyComment = Comment.builder()
                .id(id)
                .message(DUMMY)
                .author(dummyUser)
                .createdAt(LocalDateTime.MIN)
                .build();

        Mockito.when(commentRepository.findById(id)).thenReturn(Optional.of(dummyComment));
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(true);
        Mockito.doNothing().when(commentRepository).delete(dummyComment);

        commentService.delete(id, authentication);
    }

    @Test
    @DisplayName("Try delete non-existing comment by id")
    public void throwCommentNotFoundExceptionWhenDeleteComment(){
        Long id = 1L;

        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );

        Mockito.when(commentRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.delete(id, authentication));
    }

    @Test
    @DisplayName("Try delete comment by id by a user who does not have sufficient rights")
    public void throwCommentPermissionExceptionWhenDeleteComment(){
        Long id = 1L;

        Authentication authentication = new PreAuthenticatedAuthenticationToken(
                DUMMY_EMAIL,
                null
        );
        User dummyUser = User.builder()
                .id(1L)
                .email("dummy2@example.com")
                .build();
        Comment dummyComment = Comment.builder()
                .id(id)
                .message(DUMMY)
                .author(dummyUser)
                .createdAt(LocalDateTime.MIN)
                .build();

        Mockito.when(commentRepository.findById(id)).thenReturn(Optional.of(dummyComment));
        Mockito.when(authorizationUtil.isAdmin(authentication)).thenReturn(false);

        assertThrows(CommentPermissionException.class, () -> commentService.delete(id, authentication));
    }
}
