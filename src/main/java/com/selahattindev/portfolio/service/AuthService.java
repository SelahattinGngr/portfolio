package com.selahattindev.portfolio.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.CookieDto;
import com.selahattindev.portfolio.dto.UserRequestDto;
import com.selahattindev.portfolio.dto.UserResponseDto;
import com.selahattindev.portfolio.factory.CookieFactory;
import com.selahattindev.portfolio.security.service.UserDetailsImpl;
import com.selahattindev.portfolio.security.service.UserDetailsServiceImpl;
import com.selahattindev.portfolio.security.token.TokenProvider;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final AuthenticationManager authenticationManager;
        private final UserDetailsServiceImpl userDetailsService;
        private final TokenProvider tokenProvider;
        private final CookieFactory cookieFactory;
        private final CookieService cookieService;
        private final TokenService tokenService;

        public UserResponseDto signin(UserRequestDto dto, HttpServletResponse response) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

                UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

                CookieDto cookieDto = cookieFactory.create(user);

                tokenService.storeToken(user.getUsername(), cookieDto);
                cookieService.createCookies(cookieDto, response);

                return UserResponseDto
                                .builder()
                                .username(user.getUsername())
                                .role(user.getRole())
                                .build();
        }

        public void signout(String deviceId, String refreshToken, HttpServletResponse response) {
                String username = tokenProvider.extractUsernameFromRefreshToken(refreshToken);
                tokenService.deleteToken(username, deviceId);
                cookieService.clearCookies(response);
        }

        public void refreshToken(String refreshToken, String deviceId, HttpServletResponse response) {
                if (!tokenProvider.validateRefreshToken(refreshToken)) {
                        throw new RuntimeException("Invalid refresh token");
                }

                String username = tokenProvider.extractUsernameFromRefreshToken(refreshToken);
                String storedToken = tokenService.getToken(username, deviceId);

                if (storedToken == null || !storedToken.equals(refreshToken)) {
                        throw new RuntimeException("Token not found or invalid device");
                }

                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
                CookieDto cookieDto = cookieFactory.create(userDetails);
                tokenService.storeToken(userDetails.getUsername(), cookieDto);

                cookieService.refreshCookies(cookieDto, response);
        }

}
