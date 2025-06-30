package org.example.testingproject.services;

import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.dto.BookDto;
import org.example.testingproject.enums.Genre;
import org.example.testingproject.exceptions.AuthorIsNotFound;
import org.example.testingproject.exceptions.BookIsNotFound;
import org.example.testingproject.models.Book;
import org.example.testingproject.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = {"/clean.sql", "/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class BookServiceTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Test
    void testCreateBookSuccessful() {
        BookDto bookDto = new BookDto("Test", 2000, Genre.NOVEL, "Pushkin A.S.");
        Book book = bookService.createBook(bookDto);

        assertThat(book.getId()).isNotNull();
        assertThat(book.getTitle()).isEqualTo(bookDto.getTitle());
        assertThat(book.getYear()).isEqualTo(bookDto.getYear());
        assertThat(book.getGenre()).isEqualTo(bookDto.getGenre());
        assertThat(book.getAuthor().getName()).isEqualTo(bookDto.getAuthor());
    }

    @Test
    void testCreateBookUnsuccessful() {
        BookDto bookDto = new BookDto("Test", 2000, Genre.NOVEL, "DOESN'T EXIST");

        assertThatThrownBy(() -> bookService.createBook(bookDto))
                .isInstanceOf(AuthorIsNotFound.class)
                .hasMessage("Укажите существующего автора");
    }

    @Test
    void testFindAll() {
        List<BookDto> bookDtos = bookService.findAll();

        assertThat(bookDtos).isNotEmpty();
        assertThat(bookDtos)
                .anyMatch(bookDto -> bookDto.getAuthor().equals("Pushkin A.S."));
    }

    @Test
    void testFindByIdSuccessful() {
        BookDto bookDto = bookService.findById(1L);

        assertThat(bookDto.getTitle()).isEqualTo("Eugene Onegin");
        assertThat(bookDto.getYear()).isEqualTo(1833);
        assertThat(bookDto.getAuthor()).isEqualTo("Pushkin A.S.");
        assertThat(bookDto.getGenre()).isEqualTo(Genre.POETRY);
    }

    @Test
    void testFindByIdUnsuccessful() {
        assertThatThrownBy(() -> bookService.findById(111L))
                .isInstanceOf(BookIsNotFound.class)
                .hasMessage("Нет такой книги");
    }

    @Test
    void testUpdateByIdSuccessful() {
        BookDto bookDto = new BookDto("Test", 2000, Genre.NOVEL, "Pushkin A.S.");
        BookDto updatedBookDto = bookService.updateById(1L, bookDto);

        assertThat(updatedBookDto.getTitle()).isEqualTo(bookDto.getTitle());
        assertThat(updatedBookDto.getAuthor()).isEqualTo(bookDto.getAuthor());
        assertThat(updatedBookDto.getGenre()).isEqualTo(bookDto.getGenre());
        assertThat(updatedBookDto.getYear()).isEqualTo(bookDto.getYear());

        BookDto bookDtoWithoutAuthor = new BookDto("Test", 2000, Genre.NOVEL, null);
        BookDto updatedBookDtoWithoutAuthor = bookService.updateById(1L, bookDtoWithoutAuthor);

        assertThat(updatedBookDtoWithoutAuthor.getAuthor()).isNotNull();
    }

    @Test
    void testUpdateByIdUnsuccessful() {
        BookDto bookDto = new BookDto("Test", 2000, Genre.NOVEL, "Pushkin A.S.");

        assertThatThrownBy(() -> bookService.updateById(111L, bookDto))
                .isInstanceOf(BookIsNotFound.class)
                .hasMessage("Нет такой книги");

        BookDto bookDtoWithNonExistentAuthor = new BookDto("Test", 2000, Genre.NOVEL, "DOESN'T EXIST");

        assertThatThrownBy(() -> bookService.updateById(1L, bookDtoWithNonExistentAuthor))
                .isInstanceOf(AuthorIsNotFound.class)
                .hasMessage("Укажите существующего автора");
    }

    @Test
    void testDeleteByIdSuccessful() {
        bookService.deleteById(1L);
        Optional<Book> book = bookRepository.findById(1L);

        assertThat(book).isEmpty();
    }

    @Test
    void testDeleteByIdUnsuccessful() {
        assertThatThrownBy(() -> bookService.deleteById(111L))
                .isInstanceOf(BookIsNotFound.class)
                .hasMessage("Нет такой книги");
    }
}