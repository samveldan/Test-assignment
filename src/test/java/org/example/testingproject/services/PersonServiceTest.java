package org.example.testingproject.services;

import org.example.testingproject.dto.JwtAuthenticationDto;
import org.example.testingproject.dto.PersonDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql(scripts = {"/clean.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PersonServiceTest {
    @Autowired
    private PersonService personService;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Test
    void testSuccessfulLogin() {
        PersonDto personDto = new PersonDto("test", "test");
        JwtAuthenticationDto tokens = personService.login(personDto);

        assertThat(tokens.getAccessToken()).isNotNull();
        assertThat(tokens.getRefreshToken()).isNotNull();
    }

    @Test
    void testUnsuccessfulLogin() {
        PersonDto personDtoWithWrongUsername = new PersonDto("wrong", "test");
        PersonDto personDtoWithWrongPassword = new PersonDto("test", "wrong");

        assertThatThrownBy(() -> personService.login(personDtoWithWrongUsername))
                .isInstanceOf(BadCredentialsException.class);
        assertThatThrownBy(() -> personService.login(personDtoWithWrongPassword))
                .isInstanceOf(BadCredentialsException.class);
    }
}