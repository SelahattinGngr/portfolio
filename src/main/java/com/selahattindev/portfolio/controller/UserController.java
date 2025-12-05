package com.selahattindev.portfolio.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selahattindev.portfolio.dto.SigninResponseDto;
import com.selahattindev.portfolio.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/get-all-users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN'))")
    public List<SigninResponseDto> getAllUsers(@CookieValue String accessToken,
            HttpServletResponse response) {
        return userService.getAllUsers(accessToken);
    }
}
