package org.example.testingproject.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.exceptions.AuthorIsNotFound;
import org.example.testingproject.models.Author;
import org.example.testingproject.repositories.AuthorRepository;
import org.example.testingproject.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody AuthorDto authorDto) {
        Author author = authorService.createAuthor(authorDto);

        return ResponseEntity.ok("Автор с id: %s создан"
                .formatted(author.getId()));
    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(authorService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getById(@PathVariable Long id) {
        AuthorDto authorDto = authorService.findById(id);

        return ResponseEntity.ok(authorDto);
    }
}
