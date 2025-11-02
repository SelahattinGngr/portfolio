package com.selahattindev.portfolio.service;

import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.CookieDto;
import com.selahattindev.portfolio.security.util.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CookieService {
    private final CookieUtil cookieUtil;

    public void createCookies(CookieDto cookieDto, HttpServletResponse response) {
        cookieUtil.addCookie(
                response,
                "accessToken",
                cookieDto.getAccessToken(),
                cookieDto.getAccessTokenExpiry());
        cookieUtil.addCookie(response,
                "refreshToken",
                cookieDto.getRefreshToken(),
                cookieDto.getRefreshTokenExpiry());

        cookieUtil.addCookie(response,
                "deviceId",
                cookieDto.getDeviceId(),
                cookieDto.getRefreshTokenExpiry());
    }

    public void refreshCookies(CookieDto cookieDto, HttpServletResponse response) {
        cookieUtil.addCookie(
                response,
                "accessToken",
                cookieDto.getAccessToken(),
                cookieDto.getAccessTokenExpiry());
        cookieUtil.addCookie(response,
                "refreshToken",
                cookieDto.getRefreshToken(),
                cookieDto.getRefreshTokenExpiry());
    }

    public void clearCookies(HttpServletResponse response) {
        cookieUtil.clearCookie(response, "accessToken");
        cookieUtil.clearCookie(response, "refreshToken");
        cookieUtil.clearCookie(response, "deviceId");
    }

}
