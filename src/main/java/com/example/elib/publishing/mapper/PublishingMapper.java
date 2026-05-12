package com.example.elib.publishing.mapper;

import com.example.elib.publishing.dto.response.PublishingDto;
import com.example.elib.publishing.entity.Publishing;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PublishingMapper {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.name", target = "countryName")
    PublishingDto toDto(Publishing publishing);
}