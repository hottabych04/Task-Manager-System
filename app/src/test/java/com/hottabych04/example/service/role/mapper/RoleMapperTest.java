package com.hottabych04.example.service.role.mapper;

import com.hottabych04.app.database.entity.Role;
import com.hottabych04.app.service.role.mapper.RoleMapper;
import com.hottabych04.example.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RoleMapperTest extends IntegrationTestBase {

    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private RoleMapper roleMapper;

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
