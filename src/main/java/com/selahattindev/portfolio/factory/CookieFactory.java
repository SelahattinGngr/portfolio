package com.selahattindev.portfolio.factory;

import org.springframework.stereotype.Component;

import com.selahattindev.portfolio.dto.CookieDto;
import com.selahattindev.portfolio.security.jwt.JwtDto;
import com.selahattindev.portfolio.security.jwt.JwtService;
import com.selahattindev.portfolio.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CookieFactory {

    private final JwtService jwtService;
    private final JwtDto jwtDto;

    public CookieDto create(UserDetailsImpl user) {
        return new CookieDto(user, jwtService, jwtDto);
    }
}
