package org.example.testingproject.mappers;

import org.example.testingproject.models.Author;
import org.mapstruct.Named;

public class BookMapperHelper {
    @Named("mapAuthorToAuthorName")
    static String mapAuthorToAuthorName(Author author) {
        return author.getName();
    }
}
