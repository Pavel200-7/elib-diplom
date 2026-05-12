package com.example.elib.holder.service;

import com.example.elib.holder.dto.request.CreateHolderDto;
import com.example.elib.holder.dto.request.UpdateHolderDto;
import com.example.elib.holder.dto.response.HolderDto;

import java.util.List;
import java.util.UUID;

public interface HolderService {
    HolderDto createHolder(CreateHolderDto dto);
    HolderDto updateHolder(UUID id, UpdateHolderDto dto);
    HolderDto getHolderById(UUID id);
    List<HolderDto> getAllHolders();
    void deleteHolder(UUID id);
}