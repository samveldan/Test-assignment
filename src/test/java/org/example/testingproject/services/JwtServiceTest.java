package org.example.testingproject.services;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.example.testingproject.dto.JwtAuthenticationDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {
    private static final JwtService jwtService = new JwtService();

    @BeforeAll
    static void init() {
        ReflectionTestUtils.setField(jwtService, "secret", "KODE");
    }

    @Test
    void testTokenAttributesWithCorrectValues() {
        JwtAuthenticationDto tokens = jwtService.generateTokens("TEST");
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();

        assertThat(jwtService.isTokenValid(accessToken)).isTrue();
        assertThat(jwtService.isTokenValid(refreshToken)).isTrue();

        String accessUsername = jwtService.extractUsernameFromToken(accessToken);
        String refreshUsername = jwtService.extractUsernameFromToken(refreshToken);

        assertThat(accessUsername).isEqualTo("TEST");
        assertThat(refreshUsername).isEqualTo("TEST");
    }

    @Test
    void testTokenAttributesWithWrongValues() {
        String accessToken = "INVALID ACCESS TOKEN";
        String refreshToken = "INVALID REFRESH TOKEN";

        assertThatThrownBy(() -> jwtService.isTokenValid(accessToken))
                .isInstanceOf(JWTVerificationException.class);
        assertThatThrownBy(() -> jwtService.isTokenValid(refreshToken))
                .isInstanceOf(JWTVerificationException.class);
    }

    @Test
    void testRefreshAccessToken() {
        JwtAuthenticationDto tokens = jwtService.generateTokens("TEST");
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();

        JwtAuthenticationDto refreshedTokens = jwtService.refreshAccessToken(refreshToken);
        String refreshedAccessToken = refreshedTokens.getAccessToken();
        String refreshedRefreshToken = refreshedTokens.getRefreshToken();

        assertThat(accessToken).isNotEqualTo(refreshedAccessToken);
        assertThat(refreshToken).isEqualTo(refreshedRefreshToken);
    }
}