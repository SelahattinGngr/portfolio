package com.selahattindev.portfolio.service;

import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.CookieDto;
import com.selahattindev.portfolio.security.util.CookieUtil;
import com.selahattindev.portfolio.utils.CookieConstants;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CookieService {
        private final CookieUtil cookieUtil;

        public void createCookies(CookieDto cookieDto, HttpServletResponse response) {
                cookieUtil.addCookie(
                                response,
                                CookieConstants.ACCESS_TOKEN.getCookieName(),
                                cookieDto.getAccessToken(),
                                cookieDto.getAccessTokenExpiry());
                cookieUtil.addCookie(response,
                                CookieConstants.REFRESH_TOKEN.getCookieName(),
                                cookieDto.getRefreshToken(),
                                cookieDto.getRefreshTokenExpiry());

                cookieUtil.addCookie(response,
                                CookieConstants.DEVICE_ID.getCookieName(),
                                cookieDto.getDeviceId(),
                                cookieDto.getRefreshTokenExpiry());
        }

        public void refreshCookies(CookieDto cookieDto, HttpServletResponse response) {
                cookieUtil.addCookie(
                                response,
                                CookieConstants.ACCESS_TOKEN.getCookieName(),
                                cookieDto.getAccessToken(),
                                cookieDto.getAccessTokenExpiry());
                cookieUtil.addCookie(response,
                                CookieConstants.REFRESH_TOKEN.getCookieName(),
                                cookieDto.getRefreshToken(),
                                cookieDto.getRefreshTokenExpiry());
        }

        public void clearCookies(HttpServletResponse response) {
                cookieUtil.clearCookie(response, CookieConstants.ACCESS_TOKEN.getCookieName());
                cookieUtil.clearCookie(response, CookieConstants.REFRESH_TOKEN.getCookieName());
                cookieUtil.clearCookie(response, CookieConstants.DEVICE_ID.getCookieName());
        }

}
