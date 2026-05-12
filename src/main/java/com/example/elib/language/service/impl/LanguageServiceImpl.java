package com.example.elib.language.service.impl;

import com.example.elib.book.repository.BookRepository;
import com.example.elib.common.exeption.DuplicateResourceException;
import com.example.elib.common.exeption.ReferentialIntegrityException;
import com.example.elib.common.exeption.ResourceNotFoundException;
import com.example.elib.language.dto.request.CreateLanguageDto;
import com.example.elib.language.dto.request.UpdateLanguageDto;
import com.example.elib.language.dto.response.LanguageDto;
import com.example.elib.language.entity.Language;
import com.example.elib.language.mapper.LanguageMapper;
import com.example.elib.language.repository.LanguageRepository;
import com.example.elib.language.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final BookRepository bookRepository;
    private final LanguageMapper languageMapper;

    @Override
    @Transactional
    public LanguageDto createLanguage(CreateLanguageDto dto) {
        if (languageRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Язык с названием '" + dto.getName() + "' уже существует.");
        }

        Language language = Language.create(dto.getName());
        language = languageRepository.save(language);
        return languageMapper.toDto(language);
    }

    @Override
    @Transactional
    public LanguageDto updateLanguage(UUID id, UpdateLanguageDto dto) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Язык с id " + id + " не найден."));

        if (!language.getName().equals(dto.getName()) && languageRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Язык с названием '" + dto.getName() + "' уже существует.");
        }

        language.update(dto.getName());
        language = languageRepository.save(language);
        return languageMapper.toDto(language);
    }

    @Override
    @Transactional(readOnly = true)
    public LanguageDto getLanguageById(UUID id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Язык с id " + id + " не найден."));
        return languageMapper.toDto(language);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LanguageDto> getAllLanguages() {
        return languageRepository.findAll().stream()
                .map(languageMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteLanguage(UUID id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Язык с id " + id + " не найден."));
        if (bookRepository.existsByLanguageId(id)) {
            throw new ReferentialIntegrityException("Невозможно удалить язык, так как есть привязанные книги.");
        }

        languageRepository.delete(language);
    }
}