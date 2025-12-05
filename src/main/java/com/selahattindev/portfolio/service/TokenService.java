package com.selahattindev.portfolio.service;

import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.CookieDto;
import com.selahattindev.portfolio.security.jwt.JwtDto;
import com.selahattindev.portfolio.security.service.TokenStoreService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenStoreService tokenStoreService;
    private final JwtDto jwtDto;

    public void storeToken(String username, CookieDto dto) {
        tokenStoreService.storeToken(username, dto.getDeviceId(), dto.getRefreshToken(),
                jwtDto.getRefreshTokenExpirationMs());
    }

    public void deleteToken(String username, String deviceId) {
        tokenStoreService.deleteToken(username, deviceId);
    }

    public String getToken(String username, String deviceId) {
        return tokenStoreService.getToken(username, deviceId);
    }
}
