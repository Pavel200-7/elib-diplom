package com.example.elib.copy.controller;

import com.example.elib.book.dto.response.BookShortDto;
import com.example.elib.copy.dto.request.CreateCopyDto;
import com.example.elib.copy.dto.request.GetCopyCriteriaDto;
import com.example.elib.copy.dto.request.SetRegularHolderDto;
import com.example.elib.copy.dto.request.UpdateCopyDto;
import com.example.elib.copy.dto.response.CopyDto;
import com.example.elib.copy.dto.response.CopyShortDto;
import com.example.elib.copy.service.CopyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/copies")
@RequiredArgsConstructor
public class CopyController {

    private final CopyService copyService;

    @PostMapping("/page")
    public ResponseEntity<Page<CopyShortDto>> getCopyPage(@Valid @RequestBody GetCopyCriteriaDto criteria) {
        Page<CopyShortDto> result = copyService.getCopiesPage(criteria);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<CopyDto>> createCopies(@RequestBody List<CreateCopyDto> dtos) {
        List<CopyDto> created = copyService.createCopies(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/holder")
    public ResponseEntity<List<CopyDto>> setRegularHolder(@RequestBody SetRegularHolderDto dto) {
        List<CopyDto> updated = copyService.setRegularHolder(dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<CopyDto> createCopy(@RequestBody CreateCopyDto dto) {
        CopyDto created = copyService.createCopy(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CopyDto> updateCopy(
            @PathVariable UUID id,
            @RequestBody UpdateCopyDto dto) {
        CopyDto updated = copyService.updateCopy(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCopy(@PathVariable UUID id) {
        copyService.deleteCopy(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/available")
    public ResponseEntity<CopyDto> setAvailable(@PathVariable UUID id) {
        CopyDto updated = copyService.setAvailable(id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/shelved")
    public ResponseEntity<CopyDto> setShelved(@PathVariable UUID id) {
        CopyDto updated = copyService.setShelved(id);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CopyDto> getCopy(@PathVariable UUID id) {
        CopyDto copy = copyService.getCopy(id);
        return ResponseEntity.ok(copy);
    }

    @PatchMapping("/{id}/written-off")
    public ResponseEntity<CopyDto> setWrittenOff(@PathVariable UUID id) {
        CopyDto updated = copyService.setWrittenOff(id);
        return ResponseEntity.ok(updated);
    }
}