package com.example.elib.literaturegroup.service.impl;

import com.example.elib.book.repository.BookRepository;
import com.example.elib.common.exception.DuplicateResourceException;
import com.example.elib.common.exception.ReferentialIntegrityException;
import com.example.elib.common.exception.ResourceNotFoundException;
import com.example.elib.literaturegroup.dto.request.CreateLiteratureGroupDto;
import com.example.elib.literaturegroup.dto.request.UpdateLiteratureGroupDto;
import com.example.elib.literaturegroup.dto.response.LiteratureGroupDto;
import com.example.elib.literaturegroup.entity.LiteratureGroup;
import com.example.elib.literaturegroup.mapper.LiteratureGroupMapper;
import com.example.elib.literaturegroup.repository.LiteratureGroupRepository;
import com.example.elib.literaturegroup.service.LiteratureGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LiteratureGroupServiceImpl implements LiteratureGroupService {

    private final LiteratureGroupRepository literatureGroupRepository;
    private final BookRepository bookRepository;
    private final LiteratureGroupMapper literatureGroupMapper;

    @Override
    @Transactional
    public LiteratureGroupDto createLiteratureGroup(CreateLiteratureGroupDto dto) {
        if (literatureGroupRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Группа литературы с названием '" + dto.getName() + "' уже существует.");
        }

        LiteratureGroup group = LiteratureGroup.create(dto.getName());
        group = literatureGroupRepository.save(group);
        return literatureGroupMapper.toDto(group);
    }

    @Override
    @Transactional
    public LiteratureGroupDto updateLiteratureGroup(UUID id, UpdateLiteratureGroupDto dto) {
        LiteratureGroup group = literatureGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Группа литературы с id " + id + " не найдена."));

        if (!group.getName().equals(dto.getName()) && literatureGroupRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Группа литературы с названием '" + dto.getName() + "' уже существует.");
        }

        group.update(dto.getName());
        group = literatureGroupRepository.save(group);
        return literatureGroupMapper.toDto(group);
    }

    @Override
    @Transactional(readOnly = true)
    public LiteratureGroupDto getLiteratureGroupById(UUID id) {
        LiteratureGroup group = literatureGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Группа литературы с id " + id + " не найдена."));
        return literatureGroupMapper.toDto(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiteratureGroupDto> getAllLiteratureGroups() {
        return literatureGroupRepository.findAll().stream()
                .map(literatureGroupMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteLiteratureGroup(UUID id) {
        LiteratureGroup group = literatureGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Группа литературы с id " + id + " не найдена."));
         if (bookRepository.existsByLiteratureGroupId(id)) {
             throw new ReferentialIntegrityException("Невозможно удалить группу литературы, так как есть привязанные книги.");
         }

        literatureGroupRepository.delete(group);
    }
}