package com.smartfinance.userservice.controller;

import com.smartfinance.userservice.dto.RegisterRequest;
import com.smartfinance.userservice.dto.UserResponse;
import com.smartfinance.userservice.dto.LoginResponse;
import com.smartfinance.userservice.dto.LoginRequest;
import com.smartfinance.userservice.entity.User;
import com.smartfinance.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request){
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
