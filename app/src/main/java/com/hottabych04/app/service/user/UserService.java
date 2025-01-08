package com.hottabych04.app.service.user;

import com.hottabych04.app.controller.user.payload.UserCreateDto;
import com.hottabych04.app.controller.user.payload.UserGetDto;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.database.repository.UserRepository;
import com.hottabych04.app.exception.user.UserExistsException;
import com.hottabych04.app.exception.user.UserNotFoundException;
import com.hottabych04.app.service.role.RoleService;
import com.hottabych04.app.service.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final RoleService roleService;

    public UserGetDto createUser(UserCreateDto user){
        if (!isExist(user.email())){

            var encodedPassword = passwordEncoder.encode(user.password());

            User newUser = User.builder()
                    .email(user.email())
                    .hashedPassword(encodedPassword)
                    .roles(
                            List.of(roleService.getRole("USER"))
                    )
                    .build();

            User savedUser = userRepository.save(newUser);

            return userMapper.toUserGetDto(savedUser);
        }

        log.error("User with email: " + user.email() + " already exists");
        throw new UserExistsException("User already exists", user.email());
    }

    public User getUserEntity(String email){
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("User with username: " + email + " not found");
                    return new UserNotFoundException("User not found", email);
                });
    }

    public UserGetDto getUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with id: " + id + " not found");
                    return new UserNotFoundException("User not found", id.toString());
                });

        return userMapper.toUserGetDto(user);
    }

    public UserGetDto getUser(String email){
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("User with username: " + email + " not found");
                    return new UserNotFoundException("User not found", email);
                });

        return userMapper.toUserGetDto(user);
    }

    public Page<UserGetDto> getUsers(Integer page, Integer size) {

        PageRequest request = PageRequest.of(page, size);

        Page<User> userPage = userRepository.findAll(request);

        return userPage.map(userMapper::toUserGetDto);
    }

    public void deleteUser(Authentication authentication){
        String email = authentication.getName();

        userRepository.deleteByEmail(email);
    }

    public void deleteUser(Long id){
        if (isExist(id)){
            userRepository.deleteById(id);
            return;
        }

        log.error("User with id: " + id + " not found");
        throw new UserNotFoundException("User not found", id.toString());
    }

    private boolean isExist(Long id){
        return userRepository.existsById(id);
    }

    private boolean isExist(String email){
        return userRepository.existsByEmail(email);
    }
}
