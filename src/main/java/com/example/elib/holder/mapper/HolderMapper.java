package com.example.elib.holder.mapper;

import com.example.elib.holder.dto.response.HolderDto;
import com.example.elib.holder.entity.Holder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HolderMapper {

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "room.name", target = "roomName")
    HolderDto toDto(Holder holder);
}