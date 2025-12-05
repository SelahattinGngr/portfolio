package com.selahattindev.portfolio.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.selahattindev.portfolio.utils.Roles;

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
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String requestPath = request.getRequestURI();
        if (pathFiltered(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractTokenFromCookie(request, "accessToken");
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtService.validateAccessToken(accessToken)) {
            String username = jwtService.extractUsernameFromAccessToken(accessToken);
            String role = jwtService.extractRole(accessToken);

            if (role == null || role.isBlank()) {
                System.out.println("Role bilgisi JWT içinde bulunamadı, varsayılan rol atanıyor: ROLE_USER");
                role = Roles.ROLE_USER.toString();
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        Collections.singleton(new SimpleGrantedAuthority(role)));
                // JWT içindeki rolü Spring Security’nin GrantedAuthority yapısına sokuyor.
                System.out.println(authToken.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
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

    private boolean pathFiltered(String path) {
        return path.startsWith("/auth/signin") || path.startsWith("/auth/refresh") ||
                path.startsWith("/auth/signup");
    }
}
