package com.selahattindev.portfolio.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.selahattindev.portfolio.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String signinInput) throws UsernameNotFoundException {
        var user = userRepository.findByUsernameOrEmail(signinInput, signinInput)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı: " + signinInput));

        return new UserDetailsImpl(user);
    }
}