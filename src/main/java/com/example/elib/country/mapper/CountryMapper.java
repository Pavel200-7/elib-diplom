package com.example.elib.country.mapper;

import com.example.elib.country.dto.response.CountryDto;
import com.example.elib.country.entity.Country;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CountryMapper {
    CountryDto toDto(Country country);
}