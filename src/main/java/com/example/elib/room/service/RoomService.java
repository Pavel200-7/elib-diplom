package com.example.elib.room.service;

import com.example.elib.room.dto.request.CreateRoomDto;
import com.example.elib.room.dto.request.UpdateRoomDto;
import com.example.elib.room.dto.response.RoomDto;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    RoomDto createRoom(CreateRoomDto dto);
    RoomDto updateRoom(UUID id, UpdateRoomDto dto);
    RoomDto getRoomById(UUID id);
    List<RoomDto> getAllRooms();
    void deleteRoom(UUID id);
}