package com.selahattindev.portfolio.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.dto.UserResponseDto;
import com.selahattindev.portfolio.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getUsername(),
                        user.getRoles()))
                .toList();

    }

}
