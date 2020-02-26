package com.dev.cinema.controllers;

import com.dev.cinema.dto.UserRequestDto;
import com.dev.cinema.dto.UserResponseDto;
import com.dev.cinema.model.User;
import com.dev.cinema.service.AuthenticationService;
import javax.validation.Valid;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private static final Logger LOGGER = Logger.getLogger(AuthenticationController.class);
    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public UserResponseDto registration(@Valid @RequestBody UserRequestDto userDto) {
        User user = authenticationService.register(userDto.getEmail(), userDto.getPassword());
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setPassword(user.getPassword());
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
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
