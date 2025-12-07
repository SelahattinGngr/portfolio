package com.selahattindev.portfolio.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User; // Spring Security User
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.selahattindev.portfolio.utils.enums.Roles;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String path = request.getRequestURI();
            log.info("İstek Geldi: {} | Method: {}", path, request.getMethod());
            if (path.equals("/api/auth/signin") ||
                    path.equals("/api/auth/signup") ||
                    path.equals("/api/auth/refresh") ||
                    path.equals("/api/auth/verify-2fa") ||
                    path.startsWith("/actuator")) {
                filterChain.doFilter(request, response);
                return;
            }

            String accessToken = extractTokenFromCookie(request, "accessToken");
            if (accessToken != null) {
                log.info("Token Bulundu: {}...", accessToken.substring(0, 10)); // İlk 10 karakteri bas
            } else {
                log.warn("Token BULUNAMADI! Cookie gelmiyor olabilir.");
            }
            if (accessToken != null && jwtService.validateAccessToken(accessToken)) {

                String username = jwtService.extractUsernameFromAccessToken(accessToken);
                String role = jwtService.extractRoleFromAccessToken(accessToken);

                if (role == null || role.isBlank()) {
                    log.warn("Rol bulunamadı, USER atanıyor.");
                    role = Roles.ROLE_USER.toString();
                }

                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

                UserDetails userDetails = new User(username, "", authorities);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception e) {
            log.error("Security Filter Hatası: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null)
            return null;
        return Arrays.stream(request.getCookies())
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}