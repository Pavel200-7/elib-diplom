package com.example.elib.room.service.impl;

import com.example.elib.common.exeption.DuplicateResourceException;
import com.example.elib.common.exeption.ReferentialIntegrityException;
import com.example.elib.common.exeption.ResourceNotFoundException;
import com.example.elib.holder.repository.HolderRepository;
import com.example.elib.room.dto.request.CreateRoomDto;
import com.example.elib.room.dto.request.UpdateRoomDto;
import com.example.elib.room.dto.response.RoomDto;
import com.example.elib.room.entity.Room;
import com.example.elib.room.mapper.RoomMapper;
import com.example.elib.room.repository.RoomRepository;
import com.example.elib.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HolderRepository holderRepository;
    private final RoomMapper roomMapper;

    @Override
    @Transactional
    public RoomDto createRoom(CreateRoomDto dto) {
        if (roomRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Помещение с таким названием уже существует.");
        }

        Room room = Room.create(dto.getName());
        room = roomRepository.save(room);
        return roomMapper.toDto(room);
    }

    @Override
    @Transactional
    public RoomDto updateRoom(UUID id, UpdateRoomDto dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Помещение с id " + id + " не найдено."));
        if (!room.getName().equals(dto.getName()) && roomRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Помещение с названием '" + dto.getName() + "' уже существует.");
        }

        room.update(dto.getName());
        room = roomRepository.save(room);
        return roomMapper.toDto(room);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto getRoomById(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Помещение с id " + id + " не найдено."));
        return roomMapper.toDto(room);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(roomMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteRoom(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Помещение с id " + id + " не найдено."));
         if (holderRepository.existsByRoomId(id)) {
             throw new ReferentialIntegrityException("Невозможно удалить помещение, так как существуют привязанные места хранения.");
         }

        roomRepository.delete(room);
    }
}