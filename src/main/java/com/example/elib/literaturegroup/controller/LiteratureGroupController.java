package com.example.elib.literaturegroup.controller;

import com.example.elib.literaturegroup.dto.request.CreateLiteratureGroupDto;
import com.example.elib.literaturegroup.dto.request.UpdateLiteratureGroupDto;
import com.example.elib.literaturegroup.dto.response.LiteratureGroupDto;
import com.example.elib.literaturegroup.service.LiteratureGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/literature-groups")
@RequiredArgsConstructor
public class LiteratureGroupController {

    private final LiteratureGroupService literatureGroupService;

    @PostMapping
    public ResponseEntity<LiteratureGroupDto> createLiteratureGroup(@RequestBody CreateLiteratureGroupDto dto) {
        LiteratureGroupDto created = literatureGroupService.createLiteratureGroup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LiteratureGroupDto> updateLiteratureGroup(@PathVariable UUID id, @RequestBody UpdateLiteratureGroupDto dto) {
        LiteratureGroupDto updated = literatureGroupService.updateLiteratureGroup(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LiteratureGroupDto> getLiteratureGroupById(@PathVariable UUID id) {
        LiteratureGroupDto group = literatureGroupService.getLiteratureGroupById(id);
        return ResponseEntity.ok(group);
    }

    @GetMapping
    public ResponseEntity<List<LiteratureGroupDto>> getAllLiteratureGroups() {
        List<LiteratureGroupDto> groups = literatureGroupService.getAllLiteratureGroups();
        return ResponseEntity.ok(groups);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLiteratureGroup(@PathVariable UUID id) {
        literatureGroupService.deleteLiteratureGroup(id);
        return ResponseEntity.noContent().build();
    }
}