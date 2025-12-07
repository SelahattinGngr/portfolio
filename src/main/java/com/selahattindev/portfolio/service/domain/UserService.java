package com.selahattindev.portfolio.service.domain;

import java.util.List;

import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.response.SigninResponseDto;
import com.selahattindev.portfolio.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<SigninResponseDto> getAllUsers() {

        return userRepository.findAll().stream()
                .map(user -> SigninResponseDto.builder()
                        .username(user.getUsername())
                        .role(user.getRoles())
                        .build())
                .toList();
    }

}