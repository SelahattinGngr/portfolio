package com.selahattindev.portfolio.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.UserResponseDto;
import com.selahattindev.portfolio.security.jwt.JwtService;
import com.selahattindev.portfolio.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserResponseDto login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());

        return UserResponseDto.builder()
                .username(user.getUsername())
                .role(user.getRole())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String refreshAccessToken(String refreshToken) {
        if (!jwtService.validateRefreshToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String username = jwtService.extractUsernameFromRefreshToken(refreshToken);
        return jwtService.generateAccessToken(username);
    }

}
