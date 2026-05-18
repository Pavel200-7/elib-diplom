package com.example.elib.copy.mapper;

import com.example.elib.book.mapper.BookMapper;
import com.example.elib.copy.dto.response.CopyDto;
import com.example.elib.copy.dto.response.CopyShortDto;
import com.example.elib.copy.entity.Copy;
import com.example.elib.holder.mapper.HolderMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {BookMapper.class, HolderMapper.class})
public interface CopyMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "holder.id", target = "holderId")
    CopyDto toDto(Copy copy);

    @Mapping(source = "book", target = "book")
    @Mapping(source = "holder", target = "holder")
    CopyShortDto toShortDto(Copy copy);
}