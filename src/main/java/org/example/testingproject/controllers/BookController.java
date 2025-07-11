package org.example.testingproject.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.dto.BookDto;
import org.example.testingproject.models.Author;
import org.example.testingproject.models.Book;
import org.example.testingproject.services.AuthorService;
import org.example.testingproject.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Books")
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    @Operation(
            summary = "Создаём книгу",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject("Книга с id: 2 создана")
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
                                    examples = @ExampleObject("Укажите существующего автора")
                            )
                    )
            }
    )
    public ResponseEntity<String> create(@Valid @RequestBody BookDto bookDto) {
        Book book = bookService.createBook(bookDto);

        return ResponseEntity.ok("Книга с id: %s создана"
                .formatted(book.getId()));
    }

    @GetMapping
    @Operation(
            summary = "Получаем все книги",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "array",
                                            implementation = BookDto.class
                                    ),
                                    examples = @ExampleObject(
                                        """
                                        [{
                                            "title": "The Captain's Daughter",
                                            "year": 1836,
                                            "genre": "HISTORICAL_NOVEL",
                                            "author": "Pushkin A.S."
                                          },
                                          {
                                            "title": "A Hero of Our Time",
                                            "year": 1840,
                                            "genre": "NOVEL",
                                            "author": "Michail Y.L."
                                          }]
                                        """
                                    )
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
    public ResponseEntity<List<BookDto>> getAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получаем книгу по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class),
                                    examples = @ExampleObject(
                                            """
                                            {
                                              "title": "The Captain's Daughter",
                                              "year": 1836,
                                              "genre": "HISTORICAL_NOVEL",
                                              "author": "Pushkin A.S."
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
                                    examples = @ExampleObject("Нет такой книги")
                            )
                    )
            }
    )
    public ResponseEntity<BookDto> getById(@PathVariable Long id) {
        BookDto bookDto = bookService.findById(id);

        return ResponseEntity.ok(bookDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Обновляем книгу по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookDto.class),
                                    examples = @ExampleObject(
                                            """
                                            {
                                              "title": "UPDATED BOOK",
                                              "year": 1836,
                                              "genre": "HISTORICAL_NOVEL",
                                              "author": "Pushkin A.S."
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
                                    examples = @ExampleObject("Нет такой книги")
                            )
                    )
            }
    )
    public ResponseEntity<BookDto> updateById(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        BookDto updatedBook = bookService.updateById(id, bookDto);

        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаляем книгу по ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject("Книга с id: 1 удалена")
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
                                    examples = @ExampleObject("Нет такой книги")
                            )
                    )
            }
    )
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);

        return ResponseEntity.ok("Книга с id: %s удалена"
                .formatted(id));
    }
}
