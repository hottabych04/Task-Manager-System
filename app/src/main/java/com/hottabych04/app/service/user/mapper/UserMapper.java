package com.hottabych04.app.service.user.mapper;

import com.hottabych04.app.controller.user.payload.UserGetDto;
import com.hottabych04.app.controller.user.payload.UserIdsDto;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.service.role.mapper.RoleMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                RoleMapper.class
        },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UserMapper {

    UserGetDto toUserGetDto(User user);

    UserIdsDto toUserIdsDto(User user);

}

