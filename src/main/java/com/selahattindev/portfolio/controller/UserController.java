package com.selahattindev.portfolio.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selahattindev.portfolio.dto.UserResponseDto;
import com.selahattindev.portfolio.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-all-users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserResponseDto> getAllUsers() {
        System.out.println("getAllUsers called");
        return userService.getAllUsers();
    }
}
