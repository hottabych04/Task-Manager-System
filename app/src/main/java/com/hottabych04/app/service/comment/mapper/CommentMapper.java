package com.hottabych04.app.service.comment.mapper;

import com.hottabych04.app.controller.comment.payload.CommentGetDto;
import com.hottabych04.app.database.entity.Comment;
import com.hottabych04.app.service.task.mapper.TaskMapper;
import com.hottabych04.app.service.user.mapper.UserMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {
        TaskMapper.class,
        UserMapper.class
    },
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CommentMapper {

    CommentGetDto toCommentGetDto(Comment comment);

}
