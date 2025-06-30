package org.example.testingproject.services;

import lombok.RequiredArgsConstructor;
import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.dto.BookDto;
import org.example.testingproject.exceptions.AuthorIsNotFound;
import org.example.testingproject.exceptions.BookIsNotFound;
import org.example.testingproject.models.Author;
import org.example.testingproject.models.Book;
import org.example.testingproject.repositories.AuthorRepository;
import org.example.testingproject.repositories.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с книгами.
 * Обеспечивает получение (всех и по id), создание, редактирование, удаление
 * и конвертацию в DTO.
 */
@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /**
     * Создать книгу.
     * @param bookDto DTO книги
     * @throws AuthorIsNotFound если в bookDto передали несуществующего автора
     */
    public Book createBook(BookDto bookDto) {
        Author author = authorRepository.findByName(bookDto.getAuthor())
                .orElseThrow(() -> new AuthorIsNotFound("Укажите существующего автора"));

        Book book = new Book(
                bookDto.getTitle(),
                bookDto.getYear(),
                bookDto.getGenre(),
                author
        );

        return bookRepository.save(book);
    }

    /**
     * Получить все книги.
     */
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(this::convertToDtoWithAuthorName)
                .toList();
    }

    /**
     * @param id идентификатор книги
     * @throws BookIsNotFound если книга с указанным id не найдена
     */
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDtoWithAuthorName)
                .orElseThrow(() -> new BookIsNotFound("Нет такой книги"));
    }

    /**
     * Редактируем книгу.
     * @param id идентификатор книги,
     * @param bookDto DTO книги
     * @throws BookIsNotFound если книга с указанным id не найдена,
     * @throws AuthorIsNotFound если в bookDto передали несуществующего автора
     */
    public BookDto updateById(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookIsNotFound("Нет такой книги"));

        if(bookDto.getAuthor() != null) {
            Author author = authorRepository.findByName(bookDto.getAuthor())
                    .orElseThrow(() -> new AuthorIsNotFound("Укажите существующего автора"));

            book.setAuthor(author);
        }
        book.setGenre(bookDto.getGenre());
        book.setTitle(bookDto.getTitle());
        book.setYear(bookDto.getYear());

        bookRepository.save(book);
        return convertToDtoWithAuthorName(book);
    }


    /**
     * Удаляем книгу.
     * @param id идентификатор книги
     * @throws BookIsNotFound если книга с указанным id не найдена
     */
    public void deleteById(Long id) {
        bookRepository.findById(id)
                .ifPresentOrElse(
                        book -> bookRepository.deleteById(id),
                        () -> {throw new BookIsNotFound("Нет такой книги");}
                );

    }

    /**
     * Конвертация из {@link Book} в {@link BookDto}.
     * Получаем сущность автора из {@link Book} и передаем его имя
     * в {@link BookDto}
     * @param book сущность книги
     */
    private BookDto convertToDtoWithAuthorName(Book book) {
        return new BookDto(
                book.getTitle(),
                book.getYear(),
                book.getGenre(),
                book.getAuthor().getName()
        );
    }
}
