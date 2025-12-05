package com.selahattindev.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.SigninResponseDto;
import com.selahattindev.portfolio.repository.UserRepository;
import com.selahattindev.portfolio.security.token.TokenProvider;
import com.selahattindev.portfolio.utils.Roles;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public List<SigninResponseDto> getAllUsers(String accessToken) {
        String role = tokenProvider.extractRoleFromAccessToken(accessToken);
        if (role == null || !role.equals(Roles.ROLE_ADMIN.toString())) {
            throw new SecurityException("Unauthorized access");
        }
        return userRepository.findAll().stream()
                .map(user -> new SigninResponseDto(user.getUsername(),
                        user.getRoles()))
                .toList();

    }

}
