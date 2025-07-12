package org.example.testingproject.mappers;

import org.example.testingproject.dto.BookDto;
import org.example.testingproject.models.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = BookMapperHelper.class)
public interface BookMapper {
    @Mapping(source = "author", target = "author", qualifiedByName = "mapAuthorToAuthorName")
    BookDto toDto(Book book);
}
