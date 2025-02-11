package com.hottabych04.example.service.comment.mapper;

import com.hottabych04.app.controller.comment.payload.CommentGetDto;
import com.hottabych04.app.controller.user.payload.UserIdsDto;
import com.hottabych04.app.database.entity.Comment;
import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.service.comment.mapper.CommentMapperImpl;
import com.hottabych04.app.service.task.mapper.TaskMapper;
import com.hottabych04.app.service.user.mapper.UserMapper;
import com.hottabych04.example.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class CommentMapperTest extends IntegrationTestBase {

    private static final String DUMMY_EMAIL = "dummy@example.com";
    private static final String DUMMY = "dummy";

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CommentMapperImpl commentMapper;

    @Test
    @DisplayName("Success map from comment entity to CommentGetDto")
    public void commentEntityToCommentGetDto(){
        Long dummyId = 1L;
        Task dummyTask = Task.builder()
                .id(dummyId)
                .name(DUMMY)
                .build();
        User dummyUser = User.builder()
                .id(dummyId)
                .email(DUMMY_EMAIL)
                .build();
        UserIdsDto userIdsDto = new UserIdsDto(
                dummyUser.getId(),
                dummyUser.getEmail()
        );
        Comment comment = Comment.builder()
                .id(dummyId)
                .message(DUMMY)
                .author(dummyUser)
                .task(dummyTask)
                .createdAt(LocalDateTime.MIN)
                .build();

        Mockito.when(userMapper.toUserIdsDto(dummyUser)).thenReturn(userIdsDto);
        Mockito.when(taskMapper.getTaskId(dummyTask)).thenReturn(dummyId);

        CommentGetDto commentGetDto = commentMapper.toCommentGetDto(comment);

        assertAll(() -> {
            assertThat(commentGetDto).isNotNull();
            assertThat(commentGetDto.id()).isEqualTo(dummyId);
            assertThat(commentGetDto.message()).isEqualTo(comment.getMessage());
            assertThat(commentGetDto.author()).isEqualTo(userIdsDto);
            assertThat(commentGetDto.task()).isEqualTo(dummyTask.getId());
            assertThat(commentGetDto.createdAt()).isEqualTo(comment.getCreatedAt());
        });
    }
}
