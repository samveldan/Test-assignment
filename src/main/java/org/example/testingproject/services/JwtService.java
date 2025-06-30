package org.example.testingproject.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.example.testingproject.dto.JwtAuthenticationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Сервис для работы с JWT токенами.
 */
@Slf4j
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Генерируем 2 токена (access, refresh).
     * @param username имя аутентифицированного пользователя
     * @return {@link JwtAuthenticationDto} в котором хранятся 2 токена
     */
    public JwtAuthenticationDto generateTokens(String username) {
        return new JwtAuthenticationDto(
                generateAccessToken(username),
                generateRefreshToken(username)
        );
    }

    /**
     * Проверка токена на валидность.
     * @param token access/refresh
     * @throws JWTVerificationException если верификация не пройдена
     */
    public boolean isTokenValid(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        verifier.verify(token);

        return true;
    }

    /**
     * Вытаскиваем username из токена.
     * @param token access/refresh
     */
    public String extractUsernameFromToken(String token) {
        return JWT.decode(token).getSubject();
    }

    /**
     * Генерируем новый accessToken с помощью refreshToken.
     * @return {@link JwtAuthenticationDto} у которого меняется только accessToken
     */
    public JwtAuthenticationDto refreshAccessToken(String refreshToken) {
        String username = extractUsernameFromToken(refreshToken);
        isTokenValid(refreshToken);

        return new JwtAuthenticationDto(
                generateAccessToken(username),
                refreshToken
        );
    }

    /**
     * Генерируем accessToken длительностью 30 минут.
     * @param username имя аутентифицированного пользователя
     */
    private String generateAccessToken(String username) {
        Instant currentDate = new Date().toInstant();
        Instant expiresAt = currentDate.plusMillis(Duration.ofMinutes(30).toMillis());

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date().toInstant())
                .withExpiresAt(expiresAt)
                .withJWTId(UUID.randomUUID().toString())
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * Генерируем refreshToken длительностью 60 минут.
     * @param username имя аутентифицированного пользователя
     */
    private String generateRefreshToken(String username) {
        Instant currentDate = new Date().toInstant();
        Instant expiresAt = currentDate.plusMillis(Duration.ofMinutes(60).toMillis());

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date().toInstant())
                .withExpiresAt(expiresAt)
                .withJWTId(UUID.randomUUID().toString())
                .sign(Algorithm.HMAC256(secret));
    }
}
