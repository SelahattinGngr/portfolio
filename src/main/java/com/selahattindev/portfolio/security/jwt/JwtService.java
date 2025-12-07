package com.selahattindev.portfolio.security.jwt;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import com.selahattindev.portfolio.security.service.UserDetailsImpl;
import com.selahattindev.portfolio.security.token.TokenProvider;
import com.selahattindev.portfolio.utils.Roles;
import io.jsonwebtoken.Claims; // Claims importunu ekle
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

    // --- Generate Token ---
    private String generateToken(String username, String role, long expirationMs, SecretKey key) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

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

    // --- Validate Token ---
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

    @Override
    public boolean validateAccessToken(String token) {
        return validateToken(token, getAccessSigningKey());
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return validateToken(token, getRefreshSigningKey());
    }

    // --- Extract Data ---

    private Claims extractAllClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public String extractUsernameFromAccessToken(String token) {
        return extractAllClaims(token, getAccessSigningKey()).getSubject();
    }

    @Override
    public String extractUsernameFromRefreshToken(String token) {
        return extractAllClaims(token, getRefreshSigningKey()).getSubject();
    }

    @Override
    public String extractRoleFromAccessToken(String token) {
        return extractRole(token);
    }

    public String extractRole(String token) {
        try {
            Claims claims = extractAllClaims(token, getAccessSigningKey());
            String role = claims.get("role", String.class);
            if (role == null) {
                return Roles.ROLE_USER.toString();
            }
            return role;
        } catch (Exception e) {
            return Roles.ROLE_USER.toString();
        }
    }

    // --- Expiration ---
    private boolean isTokenExpired(String token, SecretKey key) {
        return extractAllClaims(token, key).getExpiration().before(new Date());
    }

    public boolean isAccessTokenExpired(String token) {
        return isTokenExpired(token, getAccessSigningKey());
    }

    public boolean isRefreshTokenExpired(String token) {
        return isTokenExpired(token, getRefreshSigningKey());
    }
}