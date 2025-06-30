package org.example.testingproject.controllers;

import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.dto.JwtAuthenticationDto;
import org.example.testingproject.dto.PersonDto;
import org.example.testingproject.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.print.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/clean.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthorControllerTest {
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
    void testCreate() throws Exception {
        JwtAuthenticationDto tokens = getTokens();
        AuthorDto authorDto = new AuthorDto("New Author", 2000, null);
        String body = mapper.writeValueAsString(authorDto);

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("Автор с id: 4 создан"));
    }

    @Test
    void testGetAll() throws Exception {
        JwtAuthenticationDto tokens = getTokens();

        mockMvc.perform(get("/authors?page=0&size=3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetById() throws Exception {
        JwtAuthenticationDto tokens = getTokens();

        mockMvc.perform(get("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken()))
                .andExpect(status().isOk());
    }
}