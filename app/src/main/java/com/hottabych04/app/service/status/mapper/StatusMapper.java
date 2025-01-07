package com.hottabych04.app.service.status.mapper;

import com.hottabych04.app.database.entity.Status;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StatusMapper {

    default String toStatusString(Status status){
        if (status == null || status.getName() == null){
            return null;
        }

        return status.getName();
    }

}
