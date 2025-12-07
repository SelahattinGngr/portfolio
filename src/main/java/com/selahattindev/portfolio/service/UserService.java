package com.selahattindev.portfolio.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.common.exception.UserAlreadyExistsException;
import com.selahattindev.portfolio.dto.SigninResponseDto;
import com.selahattindev.portfolio.dto.SignupRequestDto;
import com.selahattindev.portfolio.model.User;
import com.selahattindev.portfolio.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<SigninResponseDto> getAllUsers() {

        return userRepository.findAll().stream()
                .map(user -> SigninResponseDto.builder()
                        .username(user.getUsername())
                        .role(user.getRoles())
                        .build())
                .toList();
    }

}