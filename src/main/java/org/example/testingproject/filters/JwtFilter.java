package org.example.testingproject.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.testingproject.models.Person;
import org.example.testingproject.security.CustomUserDetailsService;
import org.example.testingproject.services.JwtService;
import org.example.testingproject.services.PersonService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для получения JWT токена из запросов.
 */
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);

        if(token != null) setSecurityContext(token);

        filterChain.doFilter(request, response);
    }

    /**
     * Если токен успешно получен - устанавливаем пользователя в контекст Security.
     * @param token access/refresh токен
     */
    private void setSecurityContext(String token) {
        String username = jwtService.extractUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /**
     *
     * @return {@link String} токен из запроса, или {@code null}, если токен отсутствует или невалиден
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(header != null && header.startsWith("Bearer ")) {
            String accessToken = header.substring(7);

            if(!jwtService.isTokenValid(accessToken)) return null;
            return accessToken;
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/auth/");
    }
}
