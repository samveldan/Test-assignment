package org.example.testingproject.services;

import jakarta.transaction.Transactional;
import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.exceptions.AuthorIsNotFound;
import org.example.testingproject.models.Author;
import org.example.testingproject.repositories.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"/clean.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthorServiceTest {
    @Autowired
    private AuthorService authorService;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Test
    void testFindByIdSuccessful() {
        AuthorDto authorDto = authorService.findById(1L);

        assertThat(authorDto.getName()).isEqualTo("Pushkin A.S.");
        assertThat(authorDto.getBirthYear()).isEqualTo(1799);
    }

    @Test
    void testFindByIdUnsuccessful() {
        assertThatThrownBy(() -> authorService.findById(111L))
                .isInstanceOf(AuthorIsNotFound.class)
                .hasMessage("Нет такого автора");
    }

    @Test
    @Transactional
    void testFindAll() {
        AuthorDto firstAuthorDto = authorService.findById(1L);
        AuthorDto secondAuthorDto = authorService.findById(2L);

        Pageable pageable = PageRequest.of(0, 2);
        List<AuthorDto> authorDtos = authorService.findAll(pageable);

        assertThat(authorDtos.size()).isEqualTo(2);
        assertThat(authorDtos.get(0)).isEqualTo(firstAuthorDto);
        assertThat(authorDtos.get(1)).isEqualTo(secondAuthorDto);
    }

    @Test
    void testCreateAuthor() {
        AuthorDto authorDto = new AuthorDto("Vasiliy", 2000, null);
        Author createdAuthor = authorService.createAuthor(authorDto);

        assertThat(createdAuthor.getId()).isNotNull();
        assertThat(authorDto.getName()).isEqualTo(createdAuthor.getName());
        assertThat(authorDto.getBirthYear()).isEqualTo(createdAuthor.getBirthYear());
        assertThat(createdAuthor.getBooks()).isNull();
    }
}