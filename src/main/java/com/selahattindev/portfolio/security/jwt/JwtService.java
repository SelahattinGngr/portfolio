package com.selahattindev.portfolio.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {

    @Value("${selahattindev.jwt.accessSecretKey}")
    private String accessSecretKey;

    @Value("${selahattindev.jwt.refreshSecretKey}")
    private String refreshSecretKey;

    private static final long ACCESS_TOKEN_EXPIRATION_MS = 15 * 60 * 1000;

    private static final long REFRESH_TOKEN_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000;

    private SecretKey getAccessSigningKey() {
        return Keys.hmacShaKeyFor(accessSecretKey.getBytes());
    }

    private SecretKey getRefreshSigningKey() {
        return Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
    }

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MS))
                .signWith(getAccessSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_MS))
                .signWith(getRefreshSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsernameFromAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(getAccessSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractUsernameFromRefreshToken(String token) {
        return Jwts.parser()
                .verifyWith(getRefreshSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getAccessSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getRefreshSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
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
