package org.example.testingproject.mappers;

import org.example.testingproject.models.Book;
import org.mapstruct.Named;

import java.util.List;

public class AuthorMapperHelper {
    @Named("mapBooksToTitles")
    static List<String> mapBooksToTitles(List<Book> books) {
        if(books == null) return null;
        return books.stream()
                .map(book -> book.getTitle())
                .toList();
    }
}
