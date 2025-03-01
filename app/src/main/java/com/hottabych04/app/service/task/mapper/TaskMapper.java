package com.hottabych04.app.service.task.mapper;

import com.hottabych04.app.controller.task.payload.TaskGetDto;
import com.hottabych04.app.database.entity.Task;
import com.hottabych04.app.service.priority.mapper.PriorityMapper;
import com.hottabych04.app.service.status.mapper.StatusMapper;
import com.hottabych04.app.service.user.mapper.UserMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {
        UserMapper.class,
        PriorityMapper.class,
        StatusMapper.class
    },
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface TaskMapper {

    TaskGetDto toTaskGetDto(Task task);

    default Long getTaskId(Task task){
        if (task != null && task.getId() != null){
            return task.getId();
        }

        return null;
    }

}
