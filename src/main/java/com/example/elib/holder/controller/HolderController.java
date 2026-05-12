package com.example.elib.holder.controller;

import com.example.elib.holder.dto.request.CreateHolderDto;
import com.example.elib.holder.dto.request.UpdateHolderDto;
import com.example.elib.holder.dto.response.HolderDto;
import com.example.elib.holder.service.HolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/holders")
@RequiredArgsConstructor
public class HolderController {

    private final HolderService holderService;

    @PostMapping
    public ResponseEntity<HolderDto> createHolder(@RequestBody CreateHolderDto dto) {
        HolderDto created = holderService.createHolder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HolderDto> updateHolder(@PathVariable UUID id, @RequestBody UpdateHolderDto dto) {
        HolderDto updated = holderService.updateHolder(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HolderDto> getHolderById(@PathVariable UUID id) {
        HolderDto holder = holderService.getHolderById(id);
        return ResponseEntity.ok(holder);
    }

    @GetMapping
    public ResponseEntity<List<HolderDto>> getAllHolders() {
        List<HolderDto> holders = holderService.getAllHolders();
        return ResponseEntity.ok(holders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHolder(@PathVariable UUID id) {
        holderService.deleteHolder(id);
        return ResponseEntity.noContent().build();
    }
}