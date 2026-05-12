package com.example.elib.language.service;

import com.example.elib.language.dto.request.CreateLanguageDto;
import com.example.elib.language.dto.request.UpdateLanguageDto;
import com.example.elib.language.dto.response.LanguageDto;

import java.util.List;
import java.util.UUID;

public interface LanguageService {
    LanguageDto createLanguage(CreateLanguageDto dto);
    LanguageDto updateLanguage(UUID id, UpdateLanguageDto dto);
    LanguageDto getLanguageById(UUID id);
    List<LanguageDto> getAllLanguages();
    void deleteLanguage(UUID id);
}