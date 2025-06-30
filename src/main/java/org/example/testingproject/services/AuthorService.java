package org.example.testingproject.services;

import lombok.RequiredArgsConstructor;
import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.exceptions.AuthorIsNotFound;
import org.example.testingproject.exceptions.BookIsNotFound;
import org.example.testingproject.models.Author;
import org.example.testingproject.models.Book;
import org.example.testingproject.repositories.AuthorRepository;
import org.example.testingproject.repositories.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с авторами.
 * Обеспечивает получение (всех и по id), создание и конвертацию в DTO.
 */
@RequiredArgsConstructor
@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final ModelMapper mapper;

    /**
     * Получить авторов с их книгами постранично.
     * @param pageable параметры пагинации (номер страницы и размер)
     */
    public List<AuthorDto> findAll(Pageable pageable) {
        Page<Author> authors = authorRepository.findAll(pageable);

        return authors.getContent().stream()
                .map(this::convertToDtoWithBookTitles).toList();
    }

    /**
     * Получить автора с его книгами.
     * @param id идентификатор автора
     * @throws AuthorIsNotFound если автор с указанным id не найден
     */
    public AuthorDto findById(Long id) {
        return authorRepository.findByIdWithBooks(id)
                .map(this::convertToDtoWithBookTitles)
                .orElseThrow(() -> new AuthorIsNotFound("Нет такого автора"));
    }

    /**
     * Создать автора.
     * @param authorDto DTO автора
     * @throws AuthorIsNotFound если автор с указанным id не найден
     */
    public Author createAuthor(AuthorDto authorDto) {
        Author author = mapper.map(authorDto, Author.class);

        return authorRepository.save(author);
    }

    /**
     * Конвертация из {@link Author} в {@link AuthorDto}.
     * Получаем сущности книг из {@link Author} и передаем их названия
     * в {@link AuthorDto}
     * @param author сущность автора
     */
    private AuthorDto convertToDtoWithBookTitles(Author author) {
        return new AuthorDto(
                author.getName(),
                author.getBirthYear(),
                author.getBooks().stream()
                        .map(Book::getTitle).toList()
        );
    }
}
