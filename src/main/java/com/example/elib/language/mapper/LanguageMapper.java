package com.example.elib.language.mapper;

import com.example.elib.language.dto.response.LanguageDto;
import com.example.elib.language.entity.Language;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LanguageMapper {
    LanguageDto toDto(Language language);
}