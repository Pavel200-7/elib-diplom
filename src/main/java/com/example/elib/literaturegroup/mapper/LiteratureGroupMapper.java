package com.example.elib.literaturegroup.mapper;

import com.example.elib.literaturegroup.dto.response.LiteratureGroupDto;
import com.example.elib.literaturegroup.entity.LiteratureGroup;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface LiteratureGroupMapper {
    LiteratureGroupDto toDto(LiteratureGroup group);
}