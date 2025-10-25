package com.selahattindev.portfolio.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtDto jwtDto;

    private SecretKey getAccessSigningKey() {
        return Keys.hmacShaKeyFor(jwtDto.getAccessSecretKey().getBytes());
    }

    private SecretKey getRefreshSigningKey() {
        return Keys.hmacShaKeyFor(jwtDto.getRefreshSecretKey().getBytes());
    }

    private String generateToken(String username, long expirationMs, SecretKey key) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateAccessToken(String username) {
        return generateToken(username, jwtDto.getACCESS_TOKEN_EXPIRATION_MS(), getAccessSigningKey());
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, jwtDto.getREFRESH_TOKEN_EXPIRATION_MS(), getRefreshSigningKey());
    }

    private String extractUsername(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractUsernameFromAccessToken(String token) {
        return this.extractUsername(token, getAccessSigningKey());
    }

    public String extractUsernameFromRefreshToken(String token) {
        return this.extractUsername(token, getRefreshSigningKey());
    }

    private boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, getAccessSigningKey());
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, getRefreshSigningKey());
    }

    private boolean isTokenExpired(String token, SecretKey key) {
        Date expiration = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
        return expiration.before(new Date());
    }

    public boolean isAccessTokenExpired(String token) {
        return isTokenExpired(token, getAccessSigningKey());
    }

    public boolean isRefreshTokenExpired(String token) {
        return isTokenExpired(token, getRefreshSigningKey());
    }
}
