package com.example.elib.holder.service.impl;

import com.example.elib.common.exception.DuplicateResourceException;
import com.example.elib.common.exception.ReferentialIntegrityException;
import com.example.elib.common.exception.ResourceNotFoundException;
import com.example.elib.copy.repository.CopyRepository;
import com.example.elib.holder.dto.request.CreateHolderDto;
import com.example.elib.holder.dto.request.UpdateHolderDto;
import com.example.elib.holder.dto.response.HolderDto;
import com.example.elib.holder.entity.Holder;
import com.example.elib.holder.mapper.HolderMapper;
import com.example.elib.holder.repository.HolderRepository;
import com.example.elib.holder.service.HolderService;
import com.example.elib.room.entity.Room;
import com.example.elib.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HolderServiceImpl implements HolderService {

    private final HolderRepository holderRepository;
    private final RoomRepository roomRepository;
    private final CopyRepository copyRepository;
    private final HolderMapper holderMapper;

    @Override
    @Transactional
    public HolderDto createHolder(CreateHolderDto dto) {
        if (holderRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Место хранения с названием '" + dto.getName() + "' уже существует.");
        }
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Помещение с id " + dto.getRoomId() + " не найдено."));

        Holder holder = Holder.create(dto.getName(), room, dto.getType());
        holder = holderRepository.save(holder);
        return holderMapper.toDto(holder);
    }

    @Override
    @Transactional
    public HolderDto updateHolder(UUID id, UpdateHolderDto dto) {
        Holder holder = holderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Место хранения с id " + id + " не найдено."));
        if (!holder.getName().equals(dto.getName()) && holderRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Место хранения с названием '" + dto.getName() + "' уже существует.");
        }
        Room room = null;
        if (holder.getRoom() == null || !holder.getRoom().getId().equals(dto.getRoomId())) {
            room = roomRepository.findById(dto.getRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Помещение с id " + dto.getRoomId() + " не найдено."));
        } else {
            room = holder.getRoom();
        }

        holder.update(dto.getName(), room, dto.getType());
        holder = holderRepository.save(holder);
        return holderMapper.toDto(holder);
    }

    @Override
    @Transactional(readOnly = true)
    public HolderDto getHolderById(UUID id) {
        Holder holder = holderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Место хранения с id " + id + " не найдено."));
        return holderMapper.toDto(holder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HolderDto> getAllHolders() {
        return holderRepository.getAllWithRoom().stream()
                .map(holderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteHolder(UUID id) {
        Holder holder = holderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Место хранения с id " + id + " не найдено."));
         if (copyRepository.existsByHolderId(id)) {
             throw new ReferentialIntegrityException("Невозможно удалить место хранения, так как есть привязанные экземпляры книг.");
         }

        holderRepository.delete(holder);
    }
}