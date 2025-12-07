package com.selahattindev.portfolio.utils.cookie;

import org.springframework.stereotype.Component;

import com.selahattindev.portfolio.dto.response.CookieDto;
import com.selahattindev.portfolio.security.UserDetailsImpl;
import com.selahattindev.portfolio.security.jwt.JwtDto;
import com.selahattindev.portfolio.security.jwt.JwtService;

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
