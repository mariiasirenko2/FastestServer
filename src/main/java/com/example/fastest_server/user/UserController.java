package com.example.fastest_server.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path="/fastest")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping(path ="/profile/{userId}")
    public String hello (@PathVariable("userId") Integer userId){

        return "Hello "+userId;
    }

    @PostMapping (path = "/signup")
    public User signUpUser(@RequestBody User user) {
        return  userService.signUpUser(user);
    }


}
