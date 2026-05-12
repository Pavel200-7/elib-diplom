package com.example.elib.genre.mapper;

import com.example.elib.genre.dto.response.GenreDto;
import com.example.elib.genre.entity.Genre;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GenreMapper {
    GenreDto toDto(Genre genre);
}