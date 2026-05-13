package com.example.elib.copy.mapper;

import com.example.elib.copy.dto.response.CopyDto;
import com.example.elib.copy.entity.Copy;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CopyMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "holder.id", target = "holderId")
    CopyDto toDto(Copy copy);
}
