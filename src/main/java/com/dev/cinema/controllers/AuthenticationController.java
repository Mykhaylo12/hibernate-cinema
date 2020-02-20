package com.dev.cinema.controllers;

import com.dev.cinema.dto.UserRequestDto;
import com.dev.cinema.model.User;
import com.dev.cinema.service.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public User registration(@RequestBody UserRequestDto userDto) {
        return authenticationService.register(userDto.getEmail(), userDto.getPassword());
    }

    @PostMapping("/login")
    public String login(@RequestBody UserRequestDto userDto) {
        try {
            authenticationService.login(userDto.getEmail(), userDto.getPassword());
            return "Success";
        } catch (javax.naming.AuthenticationException e) {
            return "Incorrect login or password";
        }
    }
}
