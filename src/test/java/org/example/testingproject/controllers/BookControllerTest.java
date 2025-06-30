package org.example.testingproject.controllers;

import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.dto.BookDto;
import org.example.testingproject.dto.JwtAuthenticationDto;
import org.example.testingproject.dto.PersonDto;
import org.example.testingproject.enums.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/clean.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BookControllerTest {
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
        BookDto bookDto = new BookDto("Test", 2000, Genre.NOVEL, "Pushkin A.S.");
        String body = mapper.writeValueAsString(bookDto);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("Книга с id: 8 создана"));
    }

    @Test
    void testGetAll() throws Exception {
        JwtAuthenticationDto tokens = getTokens();

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetById() throws Exception {
        JwtAuthenticationDto tokens = getTokens();

        mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Eugene Onegin"));
    }

    @Test
    void testUpdateById() throws Exception {
        JwtAuthenticationDto tokens = getTokens();
        BookDto bookDto = new BookDto("Test", 2000, Genre.NOVEL, "Pushkin A.S.");
        String body = mapper.writeValueAsString(bookDto);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"));
    }

    @Test
    void testDeleteById() throws Exception {
        JwtAuthenticationDto tokens = getTokens();

        mockMvc.perform(delete("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokens.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(content().string("Книга с id: 1 удалена"));
    }
}