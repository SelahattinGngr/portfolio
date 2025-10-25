package com.selahattindev.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.selahattindev.portfolio.dto.UserRequestDto;
import com.selahattindev.portfolio.dto.UserResponseDto;
import com.selahattindev.portfolio.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<UserResponseDto> signin(@RequestBody UserRequestDto dto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.signin(dto, response));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signout(@CookieValue(name = "deviceId") String deviceId,
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response) {
        authService.signout(deviceId, refreshToken, response);
        return ResponseEntity.ok("Signed out successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@CookieValue(name = "refreshToken") String refreshToken,
            @CookieValue(name = "deviceId") String deviceId,
            HttpServletResponse response) {
        authService.refreshToken(refreshToken, deviceId, response);
        return ResponseEntity.ok("Access token refreshed");
    }

    @PostMapping("/refresh/access-token")
    public ResponseEntity<String> getAccessToken(@CookieValue(name = "refreshToken") String refreshToken,
            @CookieValue(name = "deviceId") String deviceId,
            HttpServletResponse response) {
        authService.getAccessToken(refreshToken, deviceId, response);
        return ResponseEntity.ok("Access token refreshed");
    }

    // TODO: arrow function ile handle methodunu ekle
    // @PostMapping("/test")
    // public ResponseEntity<String> testEndpoint() {
    // return handle(() -> authService.testService());
    // }

    // private <T> ResponseEntity<?> handle(Supplier<T> supplier) {
    // try {
    // return ResponseEntity.ok(supplier.get());
    // } catch (EntityNotFoundException e) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    // } catch (Exception e) {
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    // }
    // }

}
