package com.hottabych04.app.controller.user;

import com.hottabych04.app.controller.user.payload.UserCreateDto;
import com.hottabych04.app.controller.user.payload.UserGetDto;
import com.hottabych04.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserGetDto createUser(@RequestBody UserCreateDto user){
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public UserGetDto getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @GetMapping
    public UserGetDto getUser(@RequestParam String email){
        return userService.getUser(email);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(Authentication authentication){
        userService.deleteUser(authentication);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
