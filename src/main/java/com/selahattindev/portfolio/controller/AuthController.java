package com.selahattindev.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selahattindev.portfolio.common.response.ApiResponse;
import com.selahattindev.portfolio.dto.SigninRequestDto;
import com.selahattindev.portfolio.dto.SigninResponseDto;
import com.selahattindev.portfolio.dto.SignupRequestDto;
import com.selahattindev.portfolio.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<SigninResponseDto>> signin(@RequestBody SigninRequestDto dto,
            HttpServletResponse response) {
        return ResponseEntity.ok(ApiResponse.success("Giriş Başarılı", authService.signin(dto, response)));
    }

    @PostMapping("/signout")
    public ResponseEntity<ApiResponse<String>> signout(@CookieValue String deviceId,
            @CookieValue String refreshToken,
            HttpServletResponse response) {
        authService.signout(deviceId, refreshToken, response);
        return ResponseEntity.ok(ApiResponse.success("Çıkış Başarılı"));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody SignupRequestDto dto) {
        authService.signup(dto);
        return ResponseEntity.ok(ApiResponse.success("Kayıt Başarılı"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refreshToken(@CookieValue String refreshToken,
            @CookieValue String deviceId,
            HttpServletResponse response) {
        authService.refreshToken(refreshToken, deviceId, response);
        return ResponseEntity.ok(ApiResponse.success("Token Yenileme Başarılı"));
    }

}
