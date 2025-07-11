package org.example.testingproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testingproject.dto.JwtAuthenticationDto;
import org.example.testingproject.dto.JwtRefreshDto;
import org.example.testingproject.dto.PersonDto;
import org.example.testingproject.services.JwtService;
import org.example.testingproject.services.PersonService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Authentication")
@RequestMapping("/auth")
public class AuthController {
    private final PersonService personService;
    private final JwtService jwtService;

    @PostMapping("/login")
    @Operation(
            summary = "Аутентифицируемся с помощью логина и пароля",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtAuthenticationDto.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject("Неправильные данные")
                            ))
            }
    )
    public ResponseEntity<JwtAuthenticationDto> login(@Valid @RequestBody PersonDto personDto) {
        JwtAuthenticationDto tokens = personService.login(personDto);

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    @Operation(
            summary = "Обновляем accessToken с помощью refreshToken",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtAuthenticationDto.class)
                            )),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject("The token was expected to have 3 parts, but got 0.")
                            )),
                    @ApiResponse(
                            responseCode = "401",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject(value = "JWT expired at 2024-07-11T14:00:00Z")
                            )
                    )
            }
    )
    public ResponseEntity<JwtAuthenticationDto> refresh(@Valid @RequestBody JwtRefreshDto jwtRefreshDto) {
        JwtAuthenticationDto tokens = jwtService.refreshAccessToken(jwtRefreshDto.getRefreshToken());

        return ResponseEntity.ok(tokens);
    }
}
