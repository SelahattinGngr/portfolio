package com.selahattindev.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.UserResponseDto;
import com.selahattindev.portfolio.repository.UserRepository;
import com.selahattindev.portfolio.security.token.TokenProvider;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public List<UserResponseDto> getAllUsers(String accessToken, HttpServletResponse response) {
        String role = tokenProvider.extractRoleFromAccessToken(accessToken);
        if (role == null || !role.equals("ADMIN")) {
            throw new SecurityException("Unauthorized access");
        }
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getUsername(),
                        user.getRoles()))
                .toList();

    }

}
