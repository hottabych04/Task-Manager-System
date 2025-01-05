package com.hottabych04.app.service.role.mapper;

import com.hottabych04.app.database.entity.Role;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    List<String> toListStringRoles(List<Role> roles);

    default String getStringRoleWithoutPrefix(Role roleWithPrefix){
        if (roleWithPrefix == null || roleWithPrefix.getName() == null){
            return null;
        }

        String role = roleWithPrefix.getName();

        return role.substring(5);
    }

}
