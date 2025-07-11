package org.example.testingproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.dto.JwtAuthenticationDto;
import org.example.testingproject.exceptions.AuthorIsNotFound;
import org.example.testingproject.models.Author;
import org.example.testingproject.repositories.AuthorRepository;
import org.example.testingproject.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Authors")
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    @Operation(
            summary = "Создаём автора",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject("Автор с id: 2 создан")
                            )),
                    @ApiResponse(
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject("Access Denied")
                            )
                    )
            }
    )
    public ResponseEntity<String> create(@Valid @RequestBody AuthorDto authorDto) {
        Author author = authorService.createAuthor(authorDto);

        return ResponseEntity.ok("Автор с id: %s создан"
                .formatted(author.getId()));
    }

    @GetMapping
    @Operation(
            summary = "Получаем авторов постранично",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "array",
                                            implementation = AuthorDto.class
                                    ),
                                    examples = @ExampleObject("""
                                            [{
                                                "name" : "Pushkin A.S.",
                                                "birthYear" : "1799",
                                                "bookTitles" : null
                                            },
                                            {
                                                "name" : "Michail Y.L.",
                                                "birthYear" : "1814",
                                                "bookTitles" : null
                                            }]
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject("Access Denied")
                            )
                    )
            }
    )
    public ResponseEntity<List<AuthorDto>> getAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(value = "size", defaultValue = "20") Integer size,
                                                  @RequestParam(required = false) String sort) {
        Pageable pageable = PageRequest.of(page, size);
        if(sort != null) pageable = PageRequest.of(page, size, Sort.by(sort));

        return ResponseEntity.ok(authorService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получаем автора по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorDto.class),
                                    examples = @ExampleObject(
                                            """
                                            {
                                              "name": "Michail Y.L.",
                                              "birthYear": 1814,
                                              "bookTitles": [
                                                "A Hero of Our Time",
                                                "Mtsyri"
                                              ]
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject("Access Denied")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject("Нет такого автора")
                            )
                    )
            }
    )
    public ResponseEntity<AuthorDto> getById(@PathVariable Long id) {
        AuthorDto authorDto = authorService.findById(id);

        return ResponseEntity.ok(authorDto);
    }
}
