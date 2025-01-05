package com.hottabych04.app.service.user;

import com.hottabych04.app.controller.user.payload.UserGetDto;
import com.hottabych04.app.database.entity.User;
import com.hottabych04.app.database.repository.UserRepository;
import com.hottabych04.app.exception.user.UserNotFoundException;
import com.hottabych04.app.service.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserGetDto getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User with id: " + id + " not found");
                    return new UserNotFoundException("User not found", id.toString());
                });

        return userMapper.toUserGetDto(user);
    }

    public UserGetDto getUserByEmail(String email){
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> {
                    log.error("User with username: " + email + " not found");
                    return new UserNotFoundException("User not found", email);
                });

        return userMapper.toUserGetDto(user);
    }
}
