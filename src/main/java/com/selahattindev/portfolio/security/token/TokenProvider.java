package com.selahattindev.portfolio.security.token;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenProvider {
    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);

    String extractUsernameFromAccessToken(String token);

    String extractUsernameFromRefreshToken(String token);

    String extractRoleFromAccessToken(String token);
}
