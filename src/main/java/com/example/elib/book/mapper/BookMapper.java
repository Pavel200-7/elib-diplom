package com.example.elib.book.mapper;

import com.example.elib.book.dto.request.CreateBookDto;
import com.example.elib.book.dto.response.BookDto;
import com.example.elib.book.dto.response.BookShortDto;
import com.example.elib.book.entity.Book;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "genre.id", target = "genreId")
    @Mapping(source = "genre.name", target = "genreName")
    @Mapping(source = "literatureGroup.id", target = "literatureGroupId")
    @Mapping(source = "literatureGroup.name", target = "literatureGroupName")
    @Mapping(source = "publishing.id", target = "publishingId")
    @Mapping(source = "publishing.name", target = "publishingName")
    @Mapping(source = "language.id", target = "languageId")
    @Mapping(source = "language.name", target = "languageName")
    BookDto toDto(Book book);

    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "genre.name", target = "genreName")
    BookShortDto toShortDto(Book book);
}