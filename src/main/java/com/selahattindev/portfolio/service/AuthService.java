package com.selahattindev.portfolio.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.common.exception.UserAlreadyExistsException;
import com.selahattindev.portfolio.dto.CookieDto;
import com.selahattindev.portfolio.dto.SigninRequestDto;
import com.selahattindev.portfolio.dto.SigninResponseDto;
import com.selahattindev.portfolio.dto.SignupRequestDto;
import com.selahattindev.portfolio.factory.CookieFactory;
import com.selahattindev.portfolio.model.User;
import com.selahattindev.portfolio.repository.UserRepository;
import com.selahattindev.portfolio.security.service.UserDetailsImpl;
import com.selahattindev.portfolio.security.service.UserDetailsServiceImpl;
import com.selahattindev.portfolio.security.token.TokenProvider;
import com.selahattindev.portfolio.utils.Roles;

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
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public SigninResponseDto signin(SigninRequestDto dto, HttpServletResponse response) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(dto.getSignin(), dto.getPassword()));

                UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

                CookieDto cookieDto = cookieFactory.create(user);
                tokenService.storeToken(user.getUsername(), cookieDto);
                cookieService.createCookies(cookieDto, response);

                return SigninResponseDto.builder()
                                .username(user.getUsername())
                                .role(user.getRole())
                                .build();
        }

        public void signup(SignupRequestDto dto) {
                // Password null kontrolü buradan SİLİNDİ (Aşağıda validasyonu anlatacağım)

                if (userRepository.existsByUsername(dto.getUsername())) {
                        throw new UserAlreadyExistsException(dto.getUsername());
                }

                var user = User.builder()
                                .username(dto.getUsername())
                                .email(dto.getEmail())
                                .password(passwordEncoder.encode(dto.getPassword()))
                                .roles(Roles.ROLE_USER.toString())
                                .build();

                userRepository.save(user);
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