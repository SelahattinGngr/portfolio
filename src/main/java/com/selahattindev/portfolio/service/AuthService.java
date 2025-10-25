package com.selahattindev.portfolio.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.CookieDto;
import com.selahattindev.portfolio.dto.UserRequestDto;
import com.selahattindev.portfolio.dto.UserResponseDto;
import com.selahattindev.portfolio.security.jwt.JwtDto;
import com.selahattindev.portfolio.security.jwt.JwtService;
import com.selahattindev.portfolio.security.service.TokenStoreService;
import com.selahattindev.portfolio.security.service.UserDetailsImpl;
import com.selahattindev.portfolio.security.util.CookieUtil;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;
        private final TokenStoreService tokenStoreService;
        private final CookieUtil cookieUtil;
        private final JwtDto jwtDto;

        public UserResponseDto signin(UserRequestDto dto, HttpServletResponse response) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

                UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

                CookieDto cookieDto = new CookieDto(user.getUsername(), jwtService, jwtDto);

                tokenStoreService.storeToken(
                                user.getUsername(),
                                cookieDto.getDeviceId(),
                                cookieDto.getRefreshToken(),
                                jwtDto.getREFRESH_TOKEN_EXPIRATION_MS());

                cookieUtil.addCookie(response,
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

                return UserResponseDto
                                .builder()
                                .username(user.getUsername())
                                .role(user.getRole())
                                .build();
        }

        public void signout(String deviceId, String refreshToken, HttpServletResponse response) {
                String username = jwtService.extractUsernameFromRefreshToken(refreshToken);
                tokenStoreService.deleteToken(username, deviceId);

                cookieUtil.clearCookie(response, "accessToken");
                cookieUtil.clearCookie(response, "refreshToken");
                cookieUtil.clearCookie(response, "deviceId");
        }

        public void refreshToken(String refreshToken, String deviceId, HttpServletResponse response) {
                if (!jwtService.validateRefreshToken(refreshToken)) {
                        throw new RuntimeException("Invalid refresh token");
                }
                String username = jwtService.extractUsernameFromRefreshToken(refreshToken);

                String storedToken = tokenStoreService.getToken(username, deviceId);
                if (storedToken == null || !storedToken.equals(refreshToken)) {
                        throw new RuntimeException("Token not found or invalid device");
                }

                CookieDto cookieDto = new CookieDto(username, jwtService, jwtDto);
                tokenStoreService.storeToken(
                                username,
                                deviceId,
                                cookieDto.getRefreshToken(),
                                jwtDto.getREFRESH_TOKEN_EXPIRATION_MS());
                cookieUtil.addCookie(response,
                                "accessToken",
                                cookieDto.getAccessToken(),
                                cookieDto.getAccessTokenExpiry());
                cookieUtil.addCookie(response,
                                "refreshToken",
                                cookieDto.getRefreshToken(),
                                cookieDto.getRefreshTokenExpiry());
        }

        public void getAccessToken(String refreshToken, String deviceId, HttpServletResponse response) {
                if (!jwtService.validateRefreshToken(refreshToken)) {
                        throw new RuntimeException("Invalid refresh token");
                }
                String username = jwtService.extractUsernameFromRefreshToken(refreshToken);

                String storedToken = tokenStoreService.getToken(username, deviceId);
                if (storedToken == null || !storedToken.equals(refreshToken)) {
                        throw new RuntimeException("Token not found or invalid device");
                }

                String newAccessToken = jwtService.generateAccessToken(username);
                cookieUtil.addCookie(response,
                                "accessToken",
                                newAccessToken,
                                (int) (jwtDto.getACCESS_TOKEN_EXPIRATION_MS() / 1000));
        }

}
