package com.selahattindev.portfolio.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "selahattindev.jwt")
public class JwtDto {
    private String accessSecretKey;
    private String refreshSecretKey;
    private long accessTokenExpirationMs;
    private long refreshTokenExpirationMs = 7 * 24 * 60 * 60 * 1000;
}
