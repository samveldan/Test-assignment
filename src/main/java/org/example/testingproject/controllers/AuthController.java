package org.example.testingproject.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testingproject.dto.JwtAuthenticationDto;
import org.example.testingproject.dto.JwtRefreshDto;
import org.example.testingproject.dto.PersonDto;
import org.example.testingproject.services.JwtService;
import org.example.testingproject.services.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonService personService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationDto> login(@Valid @RequestBody PersonDto personDto) {
        JwtAuthenticationDto tokens = personService.login(personDto);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationDto> refresh(@Valid @RequestBody JwtRefreshDto jwtRefreshDto) {
        JwtAuthenticationDto tokens = jwtService.refreshAccessToken(jwtRefreshDto.getRefreshToken());

        return ResponseEntity.ok(tokens);
    }
}
