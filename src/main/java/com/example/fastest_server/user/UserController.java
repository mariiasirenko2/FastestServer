package com.example.fastest_server.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/fastest")
@CrossOrigin
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/profile")
    public User logIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return userService.getUserIdByUsername(auth.getName());
    }

    @PostMapping(path = "/signup")
    public User signUpUser(@RequestBody User user) {
        return userService.signUpUser(user);
    }


}
