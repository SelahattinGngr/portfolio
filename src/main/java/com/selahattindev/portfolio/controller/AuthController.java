package com.selahattindev.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selahattindev.portfolio.dto.request.SigninRequestDto;
import com.selahattindev.portfolio.dto.request.SignupRequestDto;
import com.selahattindev.portfolio.dto.request.VerifyTwoFaRequestDto;
import com.selahattindev.portfolio.dto.response.GoogleTwoFaResponseDto;
import com.selahattindev.portfolio.dto.response.SigninResponseDto;
import com.selahattindev.portfolio.response.ApiResponse;
import com.selahattindev.portfolio.service.domain.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/auth")
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
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody @Valid SignupRequestDto dto) {
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

    @PostMapping("/verify-2fa")
    public ResponseEntity<ApiResponse<SigninResponseDto>> verifyTwoFa(
            @RequestBody @Valid VerifyTwoFaRequestDto dto,
            HttpServletResponse response) {

        return ResponseEntity.ok(ApiResponse.success(
                "Giriş Başarılı",
                authService.verifyTwoFa(dto, response)));
    }

    @PostMapping("/setup-google-2fa")
    public ResponseEntity<ApiResponse<GoogleTwoFaResponseDto>> setupGoogleTwoFa() {

        String username = SecurityContextHolder
                .getContext().getAuthentication().getName();
        System.out.println("=========================================");
        System.out.println("GELEN İSTEKTEKİ KULLANICI: " + username);
        System.out.println("=========================================");
        return ResponseEntity.ok(ApiResponse.success(
                "Google 2FA Kuruldu",
                authService.setupGoogleTwoFa(username)));
    }
}
