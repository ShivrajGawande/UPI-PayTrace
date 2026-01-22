package com.upipaytrace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upipaytrace.dto.JwtAuthenticationResponse;
import com.upipaytrace.dto.LoginDto;
import com.upipaytrace.dto.RegisterRequestDto;
import com.upipaytrace.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDto registerRequest) {

        String msg = userService.registerUser(registerRequest);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(
            @RequestBody LoginDto loginDto) {
    	return ResponseEntity.ok(userService.loginUser(loginDto));
    }
}

