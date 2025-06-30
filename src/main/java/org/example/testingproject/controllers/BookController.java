package org.example.testingproject.controllers;

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
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody BookDto bookDto) {
        Book book = bookService.createBook(bookDto);

        return ResponseEntity.ok("Книга с id: %s создана"
                .formatted(book.getId()));
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAll() {
        return ResponseEntity.ok(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getById(@PathVariable Long id) {
        BookDto bookDto = bookService.findById(id);

        return ResponseEntity.ok(bookDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateById(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {
        BookDto updatedBook = bookService.updateById(id, bookDto);

        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);

        return ResponseEntity.ok("Книга с id: %s удалена"
                .formatted(id));
    }

}
