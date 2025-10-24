package com.selahattindev.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selahattindev.portfolio.dto.UserRequestDto;
import com.selahattindev.portfolio.dto.UserResponseDto;
import com.selahattindev.portfolio.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto.getUsername(), dto.getPassword()));
    }

}
