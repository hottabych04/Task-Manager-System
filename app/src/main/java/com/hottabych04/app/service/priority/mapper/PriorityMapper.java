package com.hottabych04.app.service.priority.mapper;

import com.hottabych04.app.database.entity.Priority;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PriorityMapper {

    default String toPriorityString(Priority priority){
        if (priority == null || priority.getName() == null){
            return null;
        }

        return priority.getName();
    }

}
