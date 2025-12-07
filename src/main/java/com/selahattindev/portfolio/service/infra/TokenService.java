package com.selahattindev.portfolio.service.infra;

import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.response.CookieDto;
import com.selahattindev.portfolio.security.jwt.JwtDto;

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
