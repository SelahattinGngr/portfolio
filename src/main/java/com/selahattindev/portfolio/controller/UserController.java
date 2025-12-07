package com.selahattindev.portfolio.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selahattindev.portfolio.common.response.ApiResponse;
import com.selahattindev.portfolio.dto.SigninResponseDto;
import com.selahattindev.portfolio.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserService userService;

    @GetMapping("/get-all-users")
    // Token parametresine gerek yok! SecurityContext zaten kimin geldiğini biliyor.
    public ResponseEntity<ApiResponse<List<SigninResponseDto>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success("Kullanıcılar getirildi", userService.getAllUsers()));
    }
}