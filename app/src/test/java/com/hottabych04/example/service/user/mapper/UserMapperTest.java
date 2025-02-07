package com.hottabych04.example.service.user.mapper;

import com.hottabych04.app.controller.user.payload.UserGetDto;
import com.hottabych04.app.controller.user.payload.UserIdsDto;
import com.hottabych04.app.database.entity.Role;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.service.role.mapper.RoleMapper;
import com.hottabych04.app.service.user.mapper.UserMapperImpl;
import com.hottabych04.example.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest extends IntegrationTestBase {

    @Mock
    private RoleMapper roleMapper;
    @InjectMocks
    private UserMapperImpl userMapper;

    @Test
    @DisplayName("Success map from user entity to user dto")
    public void userEntityToUserGetDto(){
        List<Role> roleList = List.of(new Role(1, "ROLE_USER"));

        User user = User.builder()
                .id(1L)
                .email("dummy@example.com")
                .hashedPassword("dummyPassword")
                .roles(roleList)
                .build();

        Mockito.when(roleMapper.toListStringRoles(roleList)).thenReturn(List.of("USER"));

        UserGetDto userGetDto = userMapper.toUserGetDto(user);

        assertAll(() -> {
            assertThat(userGetDto).isNotNull();
            assertThat(userGetDto.id()).isEqualTo(user.getId());
            assertThat(userGetDto.email()).isEqualTo(user.getEmail());
            assertThat(userGetDto.roles()).isEqualTo(
                    user.getRoles().stream()
                            .map(it -> it.getName().replace("ROLE_", ""))
                            .toList()
            );
        });
    }

    @Test
    @DisplayName("Success map from user entity to user ids")
    public void userEntityToUserIds(){
        List<Role> roleList = List.of(new Role(1, "ROLE_USER"));

        User user = User.builder()
                .id(1L)
                .email("dummy@example.com")
                .hashedPassword("dummyPassword")
                .roles(roleList)
                .build();

        UserIdsDto userIdsDto = userMapper.toUserIdsDto(user);

        assertAll(() -> {
            assertThat(userIdsDto).isNotNull();
            assertThat(userIdsDto.id()).isEqualTo(user.getId());
            assertThat(userIdsDto.email()).isEqualTo(user.getEmail());
        });
    }
}
