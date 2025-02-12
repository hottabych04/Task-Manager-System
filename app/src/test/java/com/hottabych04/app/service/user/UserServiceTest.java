package com.hottabych04.app.service.user;

import com.hottabych04.app.controller.user.payload.UserCreateDto;
import com.hottabych04.app.controller.user.payload.UserGetDto;
import com.hottabych04.app.database.entity.Role;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.database.repository.UserRepository;
import com.hottabych04.app.exception.user.UserExistsException;
import com.hottabych04.app.exception.user.UserNotFoundException;
import com.hottabych04.app.service.role.RoleService;
import com.hottabych04.app.service.user.mapper.UserMapper;
import com.hottabych04.app.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserMapper userMapper = Mockito.mock(UserMapper.class);
    private RoleService roleService = Mockito.mock(RoleService.class);

    private UserService userService = new UserService(
            passwordEncoder,
            userRepository,
            userMapper,
            roleService
    );

    @Test
    @DisplayName("Success create user")
    public void successCreateUser(){
        UserCreateDto userCreateDto = new UserCreateDto("dummy@example.com", "123456789");

        Mockito.when(passwordEncoder.encode(userCreateDto.password())).thenReturn(userCreateDto.password());
        Mockito.when(roleService.getRole("USER")).thenReturn(new Role(1, "ROLE_USER"));

        User dummyUser = User.builder()
                .email(userCreateDto.email())
                .hashedPassword(userCreateDto.password())
                .roles(List.of(roleService.getRole("USER")))
                .build();

        Mockito.when(userRepository.existsByEmail(dummyUser.getEmail())).thenReturn(false);
        Mockito.when(userRepository.save(dummyUser)).thenReturn(dummyUser);

        dummyUser.setId(1L);

        UserGetDto userGetDto = new UserGetDto(
                1L,
                dummyUser.getEmail(),
                dummyUser.getRoles().stream().map(it -> it.getName().replace("ROLE_", "")).toList()
        );

        Mockito.when(userMapper.toUserGetDto(dummyUser)).thenReturn(userGetDto);

        UserGetDto user = userService.createUser(userCreateDto);

        assertThat(user).isNotNull().isEqualTo(userGetDto);
    }

    @Test
    @DisplayName("Create existing user")
    public void throwUserExistExceptionWhenCreate(){
        UserCreateDto userCreateDto = new UserCreateDto("dummy@example.com", "123456789");
        Mockito.when(userRepository.existsByEmail(userCreateDto.email())).thenReturn(true);

        assertThrows(UserExistsException.class, () -> userService.createUser(userCreateDto));
    }

    @Test
    @DisplayName("Success get userDto by id")
    public void getUserDtoById(){
        Long dummyId = 1L;

        User dummyUser = User.builder()
                .id(dummyId)
                .build();

        Mockito.when(userRepository.findById(dummyId)).thenReturn(Optional.of(dummyUser));

        UserGetDto userGetDto = new UserGetDto(dummyId, null, null);

        Mockito.when(userMapper.toUserGetDto(dummyUser)).thenReturn(userGetDto);

        UserGetDto user = userService.getUser(dummyId);

        assertThat(user).isNotNull().isEqualTo(userGetDto);
    }

    @Test
    @DisplayName("Success get userDto by email")
    public void getUserDtoByEmail(){
        String dummyEmail = "dummy@example.com";

        User dummyUser = User.builder()
                .email(dummyEmail)
                .build();

        Mockito.when(userRepository.findUserByEmail(dummyEmail)).thenReturn(Optional.of(dummyUser));

        UserGetDto userGetDto = new UserGetDto(null, dummyEmail, null);

        Mockito.when(userMapper.toUserGetDto(dummyUser)).thenReturn(userGetDto);

        UserGetDto user = userService.getUser(dummyEmail);

        assertThat(user).isNotNull().isEqualTo(userGetDto);
    }

    @Test
    @DisplayName("Success get user entity by id")
    public void getUserEntityById(){
        Long dummyId = 1L;

        User dummyUser = User.builder()
                .id(dummyId)
                .build();

        Mockito.when(userRepository.findById(dummyId)).thenReturn(Optional.of(dummyUser));

        User user = userService.getUserEntity(dummyId);

        assertThat(user).isNotNull().isEqualTo(dummyUser);
    }

    @Test
    @DisplayName("Success get user entity by email")
    public void getUserEntityByEmail(){
        String dummyEmail = "dummy@example.com";

        User dummyUser = User.builder()
                .email(dummyEmail)
                .build();

        Mockito.when(userRepository.findUserByEmail(dummyEmail)).thenReturn(Optional.of(dummyUser));

        User user = userService.getUserEntity(dummyEmail);

        assertThat(user).isNotNull().isEqualTo(dummyUser);
    }

    @Test
    @DisplayName("Find non-existing user entity by id")
    public void throwUserNotFoundExceptionWhenGetById(){
        Long dummyId = 1L;
        Mockito.when(userRepository.findById(dummyId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserEntity(dummyId));
    }

    @Test
    @DisplayName("Find non-existing user entity by email")
    public void throwUserNotFoundExceptionWhenGetByEmail(){
        String dummyEmail = "dummy@example.com";
        Mockito.when(userRepository.findUserByEmail(dummyEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserEntity(dummyEmail));
    }

    @Test
    @DisplayName("Success delete user by authentication")
    public void deleteUserByAuthentication(){
        String dummyEmail = "dummy@example.com";
        Authentication authentication = new PreAuthenticatedAuthenticationToken(dummyEmail, null);

        Mockito.doNothing().when(userRepository).deleteByEmail(dummyEmail);

        userService.deleteUser(authentication);
    }

    @Test
    @DisplayName("Success delete user by id")
    public void deleteUserById(){
        Long dummyId = 1L;

        Mockito.when(userRepository.existsById(dummyId)).thenReturn(true);
        Mockito.doNothing().when(userRepository).deleteById(dummyId);

        userService.deleteUser(dummyId);
    }

    @Test
    @DisplayName("Try to delete a user who exist")
    public void throwUserNotFountWhenDeleteUserByAuthentication(){
        Long dummyId = 1L;

        Mockito.when(userRepository.existsById(dummyId)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(dummyId));
    }
}
