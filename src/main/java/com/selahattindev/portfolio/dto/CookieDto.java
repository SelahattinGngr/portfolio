package com.selahattindev.portfolio.dto;

import java.util.UUID;

import com.selahattindev.portfolio.security.jwt.JwtDto;
import com.selahattindev.portfolio.security.jwt.JwtService;

import lombok.Data;

@Data
public class CookieDto {

    private String deviceId;
    private String accessToken;
    private String refreshToken;
    private int accessTokenExpiry;
    private int refreshTokenExpiry;

    public CookieDto(String username, JwtService jwtService, JwtDto jwtDto) {
        this.deviceId = UUID.randomUUID().toString();
        this.accessToken = jwtService.generateAccessToken(username);
        this.refreshToken = jwtService.generateRefreshToken(username);
        this.accessTokenExpiry = (int) (jwtDto.getACCESS_TOKEN_EXPIRATION_MS() / 1000);
        this.refreshTokenExpiry = (int) (jwtDto.getREFRESH_TOKEN_EXPIRATION_MS() / 1000);
    }
}
