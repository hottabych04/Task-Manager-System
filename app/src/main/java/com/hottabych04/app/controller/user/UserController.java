package com.hottabych04.app.controller.user;

import com.hottabych04.app.controller.user.payload.UserCreateDto;
import com.hottabych04.app.controller.user.payload.UserGetDto;
import com.hottabych04.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("isAnonymous()")
    public UserGetDto createUser(@RequestBody @Validated UserCreateDto user){
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserGetDto getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public UserGetDto getUser(@RequestParam String email){
        return userService.getUser(email);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<UserGetDto> getUsers(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size
    ){
        return userService.getUsers(page, size);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(Authentication authentication){
        userService.deleteUser(authentication);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
}
