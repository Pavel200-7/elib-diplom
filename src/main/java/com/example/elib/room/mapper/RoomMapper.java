package com.example.elib.room.mapper;

import com.example.elib.room.dto.request.CreateRoomDto;
import com.example.elib.room.dto.response.RoomDto;
import com.example.elib.room.entity.Room;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoomMapper {
    RoomDto toDto(Room room);
}