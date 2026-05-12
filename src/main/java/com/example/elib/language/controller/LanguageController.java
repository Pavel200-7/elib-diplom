package com.example.elib.language.controller;

import com.example.elib.language.dto.request.CreateLanguageDto;
import com.example.elib.language.dto.request.UpdateLanguageDto;
import com.example.elib.language.dto.response.LanguageDto;
import com.example.elib.language.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public ResponseEntity<LanguageDto> createLanguage(@RequestBody CreateLanguageDto dto) {
        LanguageDto created = languageService.createLanguage(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LanguageDto> updateLanguage(@PathVariable UUID id, @RequestBody UpdateLanguageDto dto) {
        LanguageDto updated = languageService.updateLanguage(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageDto> getLanguageById(@PathVariable UUID id) {
        LanguageDto language = languageService.getLanguageById(id);
        return ResponseEntity.ok(language);
    }

    @GetMapping
    public ResponseEntity<List<LanguageDto>> getAllLanguages() {
        List<LanguageDto> languages = languageService.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable UUID id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }
}