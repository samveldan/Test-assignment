package org.example.testingproject.services;

import lombok.RequiredArgsConstructor;
import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.dto.BookDto;
import org.example.testingproject.exceptions.AuthorIsNotFound;
import org.example.testingproject.exceptions.BookIsNotFound;
import org.example.testingproject.mappers.BookMapper;
import org.example.testingproject.models.Author;
import org.example.testingproject.models.Book;
import org.example.testingproject.repositories.AuthorRepository;
import org.example.testingproject.repositories.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    private final BookMapper bookMapper;

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
    @Cacheable(value = "BookDtos")
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    /**
     * @param id идентификатор книги
     * @throws BookIsNotFound если книга с указанным id не найдена
     */
    @Cacheable(value = "BookDto", key = "#id")
    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new BookIsNotFound("Нет такой книги"));
    }

    /**
     * Редактируем книгу.
     * @param id идентификатор книги,
     * @param bookDto DTO книги
     * @throws BookIsNotFound если книга с указанным id не найдена,
     * @throws AuthorIsNotFound если в bookDto передали несуществующего автора
     */
    @CachePut(value = "BookDto", key = "#id")
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
        return bookMapper.toDto(book);
    }

    /**
     * Удаляем книгу.
     * @param id идентификатор книги
     * @throws BookIsNotFound если книга с указанным id не найдена
     */
    @CacheEvict(value = "BookDto", key = "#id")
    public void deleteById(Long id) {
        bookRepository.findById(id)
                .ifPresentOrElse(
                        book -> bookRepository.deleteById(id),
                        () -> {throw new BookIsNotFound("Нет такой книги");}
                );
    }
}
