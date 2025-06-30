package org.example.testingproject.services;

import lombok.RequiredArgsConstructor;
import org.example.testingproject.dto.JwtAuthenticationDto;
import org.example.testingproject.dto.PersonDto;
import org.example.testingproject.models.Person;
import org.example.testingproject.repositories.PersonRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для работы с пользователями приложения.
 */
@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    /**
     * Аутентификация пользователя.
     * @param personDto DTO пользователя
     * @throws BadCredentialsException если данные некорректны
     * @return {@link JwtAuthenticationDto} после успешного входа
     */
    public JwtAuthenticationDto login(PersonDto personDto) {
        Person person = personRepository.findByUsername(personDto.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Неправильные данные"));

        if(encoder.matches(personDto.getPassword(), person.getPassword())) {
            return jwtService.generateTokens(person.getUsername());
        }

        throw new BadCredentialsException("Неправильные данные");
    }
}
