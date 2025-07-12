package org.example.testingproject.mappers;

import org.example.testingproject.dto.AuthorDto;
import org.example.testingproject.models.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = AuthorMapperHelper.class)
public interface AuthorMapper {
    @Mapping(source = "books", target = "bookTitles", qualifiedByName = "mapBooksToTitles")
    AuthorDto toDto(Author author);
    Author toEntity(AuthorDto authorDto);
}
