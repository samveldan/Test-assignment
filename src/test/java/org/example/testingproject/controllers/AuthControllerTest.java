package org.example.testingproject.controllers;

import org.example.testingproject.dto.JwtAuthenticationDto;
import org.example.testingproject.dto.JwtRefreshDto;
import org.example.testingproject.dto.PersonDto;
import org.example.testingproject.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/clean.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthControllerTest {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    private JwtAuthenticationDto getTokens() throws Exception {
        PersonDto personDto = new PersonDto("test", "test");
        String body = mapper.writeValueAsString(personDto);

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readValue(response, JwtAuthenticationDto.class);
    }

    @Test
    void testLogin() throws Exception {
        JwtAuthenticationDto tokens = getTokens();
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();

        assertThat(accessToken).isNotNull();
        assertThat(refreshToken).isNull();
        assertThat(jwtService.isTokenValid(accessToken)).isTrue();
        assertThat(jwtService.isTokenValid(refreshToken)).isTrue();
    }

    @Test
    void testRefresh() throws Exception {
        JwtAuthenticationDto tokens = getTokens();
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();

        JwtRefreshDto refreshDto = new JwtRefreshDto(refreshToken);
        String refreshDtoValue = mapper.writeValueAsString(refreshDto);

        String refreshResponse = mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(refreshDtoValue))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JwtAuthenticationDto newTokens = mapper.readValue(refreshResponse, JwtAuthenticationDto.class);
        String newAccessToken = newTokens.getAccessToken();
        String sameRefreshToken = newTokens.getRefreshToken();

        assertThat(accessToken).isNotEqualTo(newAccessToken);
        assertThat(jwtService.isTokenValid(accessToken)).isTrue();
        assertThat(refreshToken).isEqualTo(sameRefreshToken);
    }
}
