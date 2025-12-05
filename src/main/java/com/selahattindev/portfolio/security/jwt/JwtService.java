package com.selahattindev.portfolio.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.selahattindev.portfolio.security.service.UserDetailsImpl;
import com.selahattindev.portfolio.security.token.TokenProvider;
import com.selahattindev.portfolio.utils.Roles;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService implements TokenProvider {

    private final JwtDto jwtDto;

    private SecretKey getAccessSigningKey() {
        return Keys.hmacShaKeyFor(jwtDto.getAccessSecretKey().getBytes());
    }

    private SecretKey getRefreshSigningKey() {
        return Keys.hmacShaKeyFor(jwtDto.getRefreshSecretKey().getBytes());
    }

    private String generateToken(String username, String role, long expirationMs, SecretKey key) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // ---- Implementations of TokenProvider ----

    @Override
    public String generateAccessToken(org.springframework.security.core.userdetails.UserDetails userDetails) {
        String role = (userDetails instanceof UserDetailsImpl u) ? u.getRole() : Roles.ROLE_USER.toString();
        return generateToken(userDetails.getUsername(), role,
                jwtDto.getAccessTokenExpirationMs(), getAccessSigningKey());
    }

    @Override
    public String generateRefreshToken(org.springframework.security.core.userdetails.UserDetails userDetails) {
        String role = (userDetails instanceof UserDetailsImpl u) ? u.getRole() : Roles.ROLE_USER.toString();
        return generateToken(userDetails.getUsername(), role,
                jwtDto.getRefreshTokenExpirationMs(), getRefreshSigningKey());
    }

    @Override
    public boolean validateAccessToken(String token) {
        return validateToken(token, getAccessSigningKey());
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return validateToken(token, getRefreshSigningKey());
    }

    @Override
    public String extractUsernameFromAccessToken(String token) {
        return extractUsername(token, getAccessSigningKey());
    }

    @Override
    public String extractUsernameFromRefreshToken(String token) {
        return extractUsername(token, getRefreshSigningKey());
    }

    @Override
    public String extractRoleFromAccessToken(String token) {
        return extractRole(token);
    }

    // ---- JWT-specific utilities ----

    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith(getAccessSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    private String extractUsername(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
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
