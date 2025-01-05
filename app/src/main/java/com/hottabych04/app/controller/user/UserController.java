package com.hottabych04.app.controller.user;

import com.hottabych04.app.controller.user.payload.UserGetDto;
import com.hottabych04.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserGetDto getUser(@PathVariable Long id){
        return userService.getUserById(id);
    }

    @GetMapping
    public UserGetDto getUser(@RequestParam String email){
        return userService.getUserByEmail(email);
    }

}
