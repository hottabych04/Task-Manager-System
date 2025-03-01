package com.hottabych04.app.service.role.mapper;

import com.hottabych04.app.database.entity.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RoleMapperTest {

    private static final String ROLE_PREFIX = "ROLE_";

    private RoleMapper roleMapper = new RoleMapperImpl();

    @Test
    @DisplayName("Success map role entity to string")
    public void roleEntityToString(){
        Role dummy = new Role(1, "ROLE_DUMMY");
        String stringRole = roleMapper.getStringRoleWithoutPrefix(dummy);

        assertThat(ROLE_PREFIX + stringRole).isEqualTo(dummy.getName());
    }

    @Test
    @DisplayName("Success map list of roles entity to list of strings")
    public void listRolesEntityToListStrings(){
        List<Role> roles = List.of(
                new Role(1, "ROLE_FIRST"),
                new Role(2, "ROLE_SECOND"),
                new Role(3, "ROLE_THIRD")
        );

        List<String> listStringRoles = roleMapper.toListStringRoles(roles);

        assertAll(() -> {
            assertThat(listStringRoles).isNotNull();
            assertThat(listStringRoles.size()).isEqualTo(roles.size());
            assertThat(listStringRoles).containsAll(
                    roles.stream()
                            .map(role -> role.getName().replace(ROLE_PREFIX, ""))
                            .toList()
            );
        });
    }
}
